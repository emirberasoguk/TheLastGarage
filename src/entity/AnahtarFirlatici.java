package entity;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

// Anahtar Fırlatıcı (Topçu Kulesi Mantığı)
public class AnahtarFirlatici extends Tower {

    public AnahtarFirlatici(int x, int y) {
        // Hasar: 20, Menzil: 200, Hız: 3000ms, Maliyet: 75
        super(20, 200, 3000, 75, x, y);
    }

    @Override
    public void attack(Enemy target, List<Enemy> allEnemies) {
        if (target.isFlying()) return; // Uçanlara vuramaz

        // Alan hasarı (50px yarıçap)
        int aoeRadius = 50;
        
        // Hedefin merkezini bul
        int tx = target.getX() + 10; // Yaklaşık merkez (boyut 20-30)
        int ty = target.getY() + 10;

        for (Enemy e : allEnemies) {
            if (e.isFlying()) continue; // Uçanlar AOE'den etkilenmez

            int ex = e.getX() + 10;
            int ey = e.getY() + 10;
            double dist = Math.hypot(ex - tx, ey - ty);

            if (dist <= aoeRadius) {
                e.takeDamage(this.damage);
            }
        }
        this.lastAttackTime = System.currentTimeMillis();
    }

    @Override
    public void draw(Graphics g) {
        // Kırmızı Kare
        g.setColor(Color.RED);
        g.fillRect(x, y, 30, 30);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, 30, 30);
    }
}
