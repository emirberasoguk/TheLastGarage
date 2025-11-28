package entity;

import java.awt.Color;
import java.awt.Graphics;

// Cross Motor (Standart Düşman)
public class CrossMotor extends Enemy {

    public CrossMotor() {
        // Üst sınıfın (Enemy) kurucusunu çağırıyoruz
        // Can: 50, Hız: 50, Zırh: 0, Ödül: 10
        super(50, 50, 0, 10);
    }

    @Override
    public void move() {
        // TODO: Yol takip algoritmasına göre x ve y'yi güncelle
    }

    @Override
    public void draw(Graphics g) {
        // TODO: Ekrana bir şekil çiz (Örn: Yeşil Daire)
        g.setColor(Color.GREEN);
        g.fillOval(x, y, 20, 20); // Örnek çizim
    }
}
