package entity;

import java.awt.Color;
import java.awt.Graphics;

// Cross Motor (Standart Düşman)
public class CrossMotor extends Enemy {

    public CrossMotor() {
        // Üst sınıfın (Enemy) kurucusunu çağırıyoruz
        // Can: 50, Hız: 50, Zırh: 0, Ödül: 10, Uçma: Hayır
        super(50, 50, 0, 10, false);
    }

    @Override
    public void draw(Graphics g) {
        // Yeşil Daire
        g.setColor(Color.GREEN);
        g.fillOval((int)x, (int)y, 20, 20); 
        
        // Can Barı
        g.setColor(Color.RED);
        g.fillRect((int)x, (int)y - 5, 20, 3);
        g.setColor(Color.GREEN);
        g.fillRect((int)x, (int)y - 5, (int)(20 * ((double)hp/maxHp)), 3);
    }
}
