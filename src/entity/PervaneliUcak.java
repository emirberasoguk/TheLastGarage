package entity;

import java.awt.Color;
import java.awt.Graphics;

public class PervaneliUcak extends Enemy {

    public PervaneliUcak() {
        // Can: 50, Hız: 75, Zırh: 0, Ödül: 15, Uçma: Evet
        super(50, 75, 0, 15, true);
    }

    @Override
    public void draw(Graphics g) {
        // Mavi üçgenimsi şekil
        g.setColor(Color.BLUE);
        int ix = (int)x;
        int iy = (int)y;
        int[] xPoints = {ix, ix + 20, ix + 10};
        int[] yPoints = {iy, iy, iy + 20};
        g.fillPolygon(xPoints, yPoints, 3);
        
        // Can Barı
        g.setColor(Color.RED);
        g.fillRect(ix, iy - 5, 20, 3);
        g.setColor(Color.GREEN);
        g.fillRect(ix, iy - 5, (int)(20 * ((double)hp/maxHp)), 3);
    }
}
