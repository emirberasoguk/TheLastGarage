package entity;

import java.awt.Graphics;
import java.util.List;

// Kuleler için temel soyut sınıf
public abstract class Tower {
    
    protected int damage;       // Hasar
    protected int range;        // Menzil
    protected int fireRate;     // Atış hızı (milisaniye veya frame cinsinden)
    protected int cost;         // Maliyet
    protected int x, y;         // Kulenin haritadaki yeri
    protected long lastAttackTime; // Son atış zamanı (System.currentTimeMillis())
    
    public Tower(int damage, int range, int fireRate, int cost, int x, int y) {
        this.damage = damage;
        this.range = range;
        this.fireRate = fireRate;
        this.cost = cost;
        this.x = x;
        this.y = y;
        this.lastAttackTime = 0;
    }

    // Soyut Metotlar
    public abstract void attack(Enemy e, List<Enemy> allEnemies); // Bir düşmana saldır (veya alan hasarı için listeyi kullan)
    public abstract void draw(Graphics g); // Kuleyi ekrana çiz

    // Ortak Metotlar
    public boolean isInRange(Enemy e) {
        double dist = Math.hypot(e.getX() - x, e.getY() - y);
        return dist <= range;
    }
    
    public boolean canAttack() {
        return System.currentTimeMillis() - lastAttackTime >= fireRate;
    }
    
    public int getCost() { return cost; }
    public int getX() { return x; }
    public int getY() { return y; }
}
