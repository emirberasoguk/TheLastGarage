package entity;

import java.awt.Color;
import java.awt.Graphics;

// Civata Kulesi (Okçu Kulesi Mantığı)
public class CivataKulesi extends Tower {

    public CivataKulesi(int x, int y) {
        // Hasar: 10, Menzil: 150 (Örnek), Hız: 1000ms, Maliyet: 50
        super(10, 150, 1000, 50, x, y);
    }

    @Override
    public void attack(Enemy e) {
        // TODO: Düşmanın canını azalt
        // Özellik: Zırhlı düşmana %50 az hasar verir. Kontrol et.
    }

    @Override
    public void draw(Graphics g) {
        // TODO: Kuleyi çiz (Örn: Mavi Kare)
        g.setColor(Color.BLUE);
        g.fillRect(x, y, 30, 30);
    }
}
