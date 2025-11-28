package entity;

import java.awt.Graphics;

// Düşmanlar için temel soyut sınıf (Abstract Class)
public abstract class Enemy {
    
    // Ortak özellikler (Kapsülleme/Encapsulation için protected kullanıyoruz, alt sınıflar erişsin diye)
    protected int hp;           // Can
    protected int maxHp;        // Maksimum can (Bar gösterimi için)
    protected int speed;        // Hız
    protected double armor;     // Zırh
    protected int reward;       // Öldüğünde vereceği para
    protected int x, y;         // Haritadaki konumu
    
    // Kurucu Metot (Constructor)
    public Enemy(int hp, int speed, double armor, int reward) {
        this.hp = hp;
        this.maxHp = hp;
        this.speed = speed;
        this.armor = armor;
        this.reward = reward;
        // Başlangıç konumu (x,y) harita başlangıcına göre ayarlanacak
        this.x = 0; 
        this.y = 0; 
    }

    // Soyut Metotlar (Alt sınıflar bunları kendine göre doldurmak ZORUNDA)
    // Polimorfizm burada devreye giriyor.
    public abstract void move(); // Her düşman farklı hareket edebilir mi? (Genelde yol aynıdır ama hız farklıdır)
    
    // Çizim metodu
    public abstract void draw(Graphics g); 

    // Somut Metotlar (Her düşman için aynı olan işlemler)
    public void takeDamage(int damage) {
        // Zırh formülü burada uygulanacak
        // TODO: Formülü uygula: Net_Hasar = Hasar * (1 - (Zırh / (Zırh + 100)))
        this.hp -= damage;
    }

    public boolean isDead() {
        return hp <= 0;
    }

    // Getter ve Setter'lar
    public int getX() { return x; }
    public int getY() { return y; }
    public int getReward() { return reward; }
}
