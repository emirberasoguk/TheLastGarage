package entity;

import java.awt.Graphics;

// Kuleler için temel soyut sınıf
public abstract class Tower {
    
    protected int damage;       // Hasar
    protected int range;        // Menzil
    protected int fireRate;     // Atış hızı (milisaniye veya frame cinsinden)
    protected int cost;         // Maliyet
    protected int x, y;         // Kulenin haritadaki yeri
    
    public Tower(int damage, int range, int fireRate, int cost, int x, int y) {
        this.damage = damage;
        this.range = range;
        this.fireRate = fireRate;
        this.cost = cost;
        this.x = x;
        this.y = y;
    }

    // Soyut Metotlar
    public abstract void attack(Enemy e); // Bir düşmana saldır
    public abstract void draw(Graphics g); // Kuleyi ekrana çiz

    // Ortak Metotlar
    public boolean isInRange(Enemy e) {
        // Pisagor teoremi ile mesafe hesapla
        // TODO: (x1-x2)^2 + (y1-y2)^2 karekökü menzilden küçük mü kontrol et
        return false; 
    }
    
    public int getCost() { return cost; }
}
