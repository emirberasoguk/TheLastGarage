package entity;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

// Yağlama Kulesi (Buz Kulesi Mantığı)
public class YaglamaKulesi extends Tower {

    public YaglamaKulesi(int x, int y) {
        // Hasar: 15, Menzil: 150, Hız: 2000ms, Maliyet: 70
        super(15, 150, 2000, 70, x, y);
    }

    @Override
    public void attack(Enemy e, List<Enemy> allEnemies) {
        e.takeDamage(this.damage);
        e.slowDown(3000); // 3 saniye yavaşlat
        this.lastAttackTime = System.currentTimeMillis();
    }

    @Override
    public void draw(Graphics g) {
        // Sarı/Siyah Kare (Yağ teması)
        g.setColor(Color.YELLOW);
        g.fillRect(x, y, 30, 30);
        g.setColor(Color.BLACK);
        g.fillOval(x+5, y+5, 20, 20); // İçinde siyah damla
    }
}
