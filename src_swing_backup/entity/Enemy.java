package entity;

import java.awt.Graphics;
import java.awt.Point;
import java.util.List;

// Düşmanlar için temel soyut sınıf (Abstract Class)
public abstract class Enemy {
    
    // Ortak özellikler
    protected int hp;           // Can
    protected int maxHp;        // Maksimum can
    protected int speed;        // Hız
    protected double armor;     // Zırh
    protected int reward;       // Ödül
    protected boolean isFlying; // Uçuyor mu?
    protected double x, y;      // Haritadaki konumu (Double for smoother movement)
    
    protected List<Point> path; // İzleyeceği yol
    protected int currentTargetIndex = 0; // Yoldaki hangi noktaya gidiyor
    protected boolean reachedEnd = false; // Sona ulaştı mı
    
    protected int baseSpeed;
    protected long slowEndTime = 0;

    // Kurucu Metot (Constructor)
    public Enemy(int hp, int speed, double armor, int reward, boolean isFlying) {
        this.hp = hp;
        this.maxHp = hp;
        this.speed = speed;
        this.baseSpeed = speed;
        this.armor = armor;
        this.reward = reward;
        this.isFlying = isFlying;
        this.x = 0; 
        this.y = 0; 
    }
    
    public void slowDown(long durationMs) {
        this.slowEndTime = System.currentTimeMillis() + durationMs;
    }

    // Yolu ata
    public void setPath(List<Point> path) {
        this.path = path;
        if (path != null && !path.isEmpty()) {
            this.x = path.get(0).x;
            this.y = path.get(0).y;
            this.currentTargetIndex = 1; // İlk noktadan başla, ikinciye git
        }
    }

    // Hareketi işle
    public void move() {
        if (path == null || path.isEmpty() || reachedEnd) return;
        
        // Yavaşlatma kontrolü
        int currentSpeed = baseSpeed;
        if (System.currentTimeMillis() < slowEndTime) {
            currentSpeed = baseSpeed / 2;
        }

        Point target;
        if (isFlying) {
            // Uçanlar doğrudan son noktaya (Garaj) gider
            target = path.get(path.size() - 1);
        } else {
            // Yerdekiler sırayla gider
            if (currentTargetIndex >= path.size()) {
                reachedEnd = true;
                return;
            }
            target = path.get(currentTargetIndex);
        }

        double dx = target.x - x;
        double dy = target.y - y;
        double distance = Math.sqrt(dx*dx + dy*dy);
        
        double moveAmount = (double)currentSpeed / 60.0; 

        if (distance <= moveAmount) {
            x = target.x;
            y = target.y;
            if (!isFlying) currentTargetIndex++;
            else reachedEnd = true;
        } else {
            x += (dx / distance) * moveAmount;
            y += (dy / distance) * moveAmount;
        }
    }
    
    public boolean isSlowed() { return System.currentTimeMillis() < slowEndTime; }
    
    // Çizim metodu
    public abstract void draw(Graphics g); 

    // Somut Metotlar
    public void takeDamage(int damage) {
        // Zırh formülü: Net_Hasar = Hasar * (1 - (Zırh / (Zırh + 100)))
        int netDamage = (int) (damage * (1.0 - (armor / (armor + 100.0))));
        if (netDamage < 1) netDamage = 1; // En az 1 hasar
        this.hp -= netDamage;
    }

    public boolean isDead() {
        return hp <= 0;
    }
    
    public boolean hasReachedEnd() { return reachedEnd; }

    // Getter ve Setter'lar
    public int getX() { return (int)x; }
    public int getY() { return (int)y; }
    public int getReward() { return reward; }
    public boolean isFlying() { return isFlying; }
    public double getArmor() { return armor; }
}
