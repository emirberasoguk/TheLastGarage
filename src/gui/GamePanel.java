package gui;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;

// Oyunun çizim alanı
public class GamePanel extends JPanel implements Runnable {

    // Oyun Döngüsü değişkenleri
    private Thread gameThread;
    private boolean isRunning = false;
    private final int FPS = 60;

    public GamePanel() {
        this.setBackground(Color.LIGHT_GRAY); // Arka plan rengi (Sanayi teması için gri)
        this.setDoubleBuffered(true); // Ekran titremesini önler
    }

    public void startGame() {
        gameThread = new Thread(this);
        isRunning = true;
        gameThread.start();
    }

    @Override
    public void run() {
        // OYUN DÖNGÜSÜ (GAME LOOP)
        // Burası oyunun kalbidir. Sürekli çalışır, mantığı günceller ve ekrana çizer.
        double drawInterval = 1000000000 / FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (isRunning) {
            update();       // 1. Konumları güncelle
            repaint();      // 2. Ekrana çiz (paintComponent'i çağırır)

            try {
                // FPS'i sabitlemek için bekleme süresi
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

    // Oyun mantığını güncelleme (Düşman hareketi, kule atışı vb.)
    public void update() {
        // TODO: GameManager üzerinden düşmanları hareket ettir
    }

    // Ekrana çizim yapma
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // TODO: Yolu çiz
        // TODO: Kuleleri çiz
        // TODO: Düşmanları çiz
    }
}
