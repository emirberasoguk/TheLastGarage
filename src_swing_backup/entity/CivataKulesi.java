package entity;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

// Civata Kulesi (Okçu Kulesi Mantığı)
public class CivataKulesi extends Tower {

    public CivataKulesi(int x, int y) {
        // Hasar: 10, Menzil: 150 (Örnek), Hız: 1000ms, Maliyet: 50
        super(10, 150, 1000, 50, x, y);
    }

    @Override
    public void attack(Enemy e, List<Enemy> allEnemies) {
        int appliedDamage = this.damage;
        // Özellik: Zırhlı düşmana %50 az hasar verir.
        if (e.getArmor() > 0) {
            appliedDamage /= 2;
        }
        e.takeDamage(appliedDamage);
        this.lastAttackTime = System.currentTimeMillis();
    }

    @Override
    public void draw(Graphics g) {
        // Mavi Kare
        g.setColor(Color.BLUE);
        g.fillRect(x, y, 30, 30);
    }
}
