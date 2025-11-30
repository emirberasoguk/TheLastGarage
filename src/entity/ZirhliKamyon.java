package entity;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class ZirhliKamyon extends Enemy {

    public ZirhliKamyon() {
        // Can: 75, Hız: 25, Zırh: 50-100 (Rastgele), Ödül: 20, Uçma: Hayır
        super(75, 25, 0, 20, false); // Zırhı aşağıda hesaplayıp atayacağız
        
        // Zırh 50 ile 100 arasında rastgele
        Random rand = new Random();
        this.armor = 50 + rand.nextInt(51); 
    }

    @Override
    public void draw(Graphics g) {
        // Gri/Kahverengi dikdörtgen
        g.setColor(new Color(139, 69, 19)); // SaddleBrown
        g.fillRect((int)x, (int)y, 30, 20);
        
        // Zırhı göstermek için basit bir çizgi
        g.setColor(Color.GRAY);
        g.drawRect((int)x, (int)y, 30, 20);
        
        // Can Barı
        g.setColor(Color.RED);
        g.fillRect((int)x, (int)y - 5, 30, 3);
        g.setColor(Color.GREEN);
        g.fillRect((int)x, (int)y - 5, (int)(30 * ((double)hp/maxHp)), 3);
    }
}
