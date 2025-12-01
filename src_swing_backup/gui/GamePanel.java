package gui;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Iterator;

import entity.*;
import util.GameLogger;

// Oyunun çizim alanı ve ana mantığı
public class GamePanel extends JPanel implements Runnable {

    // Oyun Döngüsü
    private Thread gameThread;
    private boolean isRunning = false;
    private final int FPS = 60;

    // Oyun Durumu
    private int scrap = 200;
    private int garageHp = 100;
    private int wave = 0;
    private boolean isGameOver = false;
    private boolean isWaveActive = false;

    // Varlıklar
    private List<Enemy> enemies = new ArrayList<>();
    private List<Tower> towers = new ArrayList<>();
    private List<Point> path = new ArrayList<>();
    
    // Dalga Yönetimi
    private List<Enemy> spawnQueue = new ArrayList<>();
    private long lastSpawnTime = 0;
    private final int SPAWN_INTERVAL = 1500; // 1.5 saniyede bir düşman

    // Kullanıcı Etkileşimi
    private String selectedTowerType = null; // "Civata", "Anahtar", "Yaglama"

    public GamePanel() {
        this.setBackground(Color.LIGHT_GRAY);
        this.setDoubleBuffered(true);
        this.setPreferredSize(new Dimension(800, 600));
        
        initPath();
        setupInput();
        GameLogger.clearLog();
        GameLogger.log("Simülasyon Başladı. Can: " + garageHp + ", Hurda: " + scrap);
    }

    private void initPath() {
        // Yol Noktaları
        path.add(new Point(0, 300));    // Başlangıç
        path.add(new Point(200, 300));
        path.add(new Point(200, 100));
        path.add(new Point(500, 100));
        path.add(new Point(500, 400));
        path.add(new Point(700, 400));
        path.add(new Point(700, 300));  // Bitiş (Garaj)
    }
    
    private void setupInput() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isGameOver) return;
                if (selectedTowerType != null) {
                    placeTower(e.getX(), e.getY());
                }
            }
        });
    }

    public void startGame() {
        if (gameThread == null) {
            gameThread = new Thread(this);
            isRunning = true;
            gameThread.start();
        }
    }

    public void startNextWave() {
        if (isWaveActive || isGameOver) return;
        
        wave++;
        GameLogger.log("Dalga " + wave + " Başladı.");
        generateWave(wave);
        isWaveActive = true;
    }
    
    private void generateWave(int waveNum) {
        spawnQueue.clear();
        if (waveNum == 1) {
            // Sabit: 2 Cross, 1 Kamyon, 1 Uçak
            spawnQueue.add(new CrossMotor());
            spawnQueue.add(new CrossMotor());
            spawnQueue.add(new ZirhliKamyon());
            spawnQueue.add(new PervaneliUcak());
        } else {
            // Rastgele 5-10 düşman
            Random rand = new Random();
            int count = 5 + rand.nextInt(6); // 5-10
            // Her türden en az 1 tane
            spawnQueue.add(new CrossMotor());
            spawnQueue.add(new ZirhliKamyon());
            spawnQueue.add(new PervaneliUcak());
            
            for (int i = 3; i < count; i++) {
                int r = rand.nextInt(3);
                if (r == 0) spawnQueue.add(new CrossMotor());
                else if (r == 1) spawnQueue.add(new ZirhliKamyon());
                else spawnQueue.add(new PervaneliUcak());
            }
        }
    }

    private void placeTower(int x, int y) {
        // Basit ızgara hizalaması (opsiyonel) veya serbest yerleşim
        // Kontrol: Yola koyma (Basitçe yol noktalarına yakınlık kontrolü yapılabilir ama path çizgi olduğu için zor)
        // Şimdilik serbest.
        
        Tower t = null;
        if ("Civata".equals(selectedTowerType)) {
            if (scrap >= 50) t = new CivataKulesi(x - 15, y - 15); // Merkezleme
        } else if ("Anahtar".equals(selectedTowerType)) {
            if (scrap >= 75) t = new AnahtarFirlatici(x - 15, y - 15);
        } else if ("Yaglama".equals(selectedTowerType)) {
            if (scrap >= 70) t = new YaglamaKulesi(x - 15, y - 15);
        }
        
        if (t != null) {
            towers.add(t);
            scrap -= t.getCost();
            GameLogger.log(selectedTowerType + " Kulesi inşa edildi. Kalan Hurda: " + scrap);
            selectedTowerType = null; // Seçimi sıfırla
        } else {
            // Yetersiz bakiye uyarısı (Log veya GUI)
        }
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (isRunning) {
            update();
            repaint();

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;
                if (remainingTime < 0) remainingTime = 0;
                Thread.sleep((long) remainingTime);
                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        if (isGameOver) return;

        // 1. Spawn
        if (isWaveActive && !spawnQueue.isEmpty()) {
            if (System.currentTimeMillis() - lastSpawnTime > SPAWN_INTERVAL) {
                Enemy e = spawnQueue.remove(0);
                e.setPath(path); // Yolu ver
                enemies.add(e);
                lastSpawnTime = System.currentTimeMillis();
                GameLogger.log("Düşman Girişi: " + e.getClass().getSimpleName());
            }
        } else if (isWaveActive && spawnQueue.isEmpty() && enemies.isEmpty()) {
            // Dalga bitti
            isWaveActive = false;
            GameLogger.log("Dalga " + wave + " Bitti.");
            if (wave >= 2) {
                GameLogger.log("Oyun Kazanıldı! Tüm dalgalar temizlendi.");
                isGameOver = true; // KAZANDIN
            }
        }

        // 2. Enemies Move & Update
        Iterator<Enemy> it = enemies.iterator();
        while (it.hasNext()) {
            Enemy e = it.next();
            e.move();
            
            if (e.hasReachedEnd()) {
                // Üsse ulaştı
                int dmg = 0;
                if (e instanceof CrossMotor) dmg = 5;
                else if (e instanceof ZirhliKamyon) dmg = 10;
                else if (e instanceof PervaneliUcak) dmg = 5;
                
                garageHp -= dmg;
                GameLogger.log("Düşman Üsse Ulaştı! Hasar: " + dmg + ". Kalan Can: " + garageHp);
                it.remove();
                
                if (garageHp <= 0) {
                    isGameOver = true;
                    GameLogger.log("Oyun Kaybedildi! Garaj yıkıldı.");
                }
            }
        }

        // 3. Towers Attack
        for (Tower t : towers) {
            if (t.canAttack()) {
                // Menzildeki düşmanları bul
                Enemy target = null;
                // En öndeki (üsse en yakın) düşmanı seçme mantığı basitçe listedeki ilk uygun düşman olsun
                for (Enemy e : enemies) {
                    if (t.isInRange(e)) {
                        // Anahtar kulesi uçanlara vuramaz kontrolü burada veya içeride yapılabilir
                        // Ama hedef seçerken dikkat etmeli.
                        // Civata kulesi "Menzildeki tek düşman (Üsse en yakın)" diyor.
                        // Listeye eklenme sırası kabaca ilerleme sırası (ilk giren en önde).
                        if (t instanceof AnahtarFirlatici && e.isFlying()) continue;
                        
                        target = e;
                        break; 
                    }
                }
                
                if (target != null) {
                    t.attack(target, enemies);
                    GameLogger.log("Kule Atışı: " + t.getClass().getSimpleName() + " -> " + target.getClass().getSimpleName());
                    
                    if (target.isDead()) {
                        scrap += target.getReward();
                        GameLogger.log("Düşman Öldü: " + target.getClass().getSimpleName() + ". Ödül: " + target.getReward());
                        enemies.remove(target);
                    }
                }
            }
        }
        
        // Ölüleri temizle (AOE'den ölenler olabilir, yukarıdaki remove sadece hedefi sildi)
        Iterator<Enemy> cleanIt = enemies.iterator();
        while (cleanIt.hasNext()) {
            Enemy e = cleanIt.next();
            if (e.isDead()) {
                scrap += e.getReward();
                GameLogger.log("Düşman Öldü (AOE): " + e.getClass().getSimpleName());
                cleanIt.remove();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Yolu Çiz
        g.setColor(Color.DARK_GRAY);
        if (path.size() > 0) {
            Point p1 = path.get(0);
            for (int i = 1; i < path.size(); i++) {
                Point p2 = path.get(i);
                g.drawLine(p1.x, p1.y, p2.x, p2.y);
                p1 = p2;
            }
        }
        
        // Garajı Çiz
        g.setColor(Color.BLACK);
        g.fillRect(700, 280, 50, 40);
        g.setColor(Color.WHITE);
        g.drawString("GARAJ", 705, 305);
        
        // Kuleleri Çiz
        for (Tower t : towers) {
            t.draw(g);
        }
        
        // Düşmanları Çiz
        for (Enemy e : enemies) {
            e.draw(g);
        }
        
        // UI Bilgileri (Üst köşe)
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Can: " + garageHp, 10, 20);
        g.drawString("Hurda: " + scrap, 100, 20);
        g.drawString("Dalga: " + wave, 200, 20);
        
        if (isGameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            if (garageHp > 0) g.drawString("KAZANDINIZ!", 300, 300);
            else g.drawString("KAYBETTİNİZ!", 300, 300);
        }
    }
    
    public void setSelectedTower(String type) {
        this.selectedTowerType = type;
    }
}
