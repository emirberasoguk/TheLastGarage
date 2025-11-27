package com.kouceng.td.varliklar.kuleler;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.kouceng.td.varliklar.dusman.dusman; // düşmanları kontrol etmek için

import java.util.List;


public class topcuKule extends kule {
    private Texture texture;
    private float attackCooldown = 0; // saldırı bekleme süresi

    public topcuKule(float x, float y, List<dusman> dusmanlar) {
        super(x, y, dusmanlar);

        this.texture = new Texture("topcu_kule.png"); // resim dosyası
        this.damage = 20;       // hasar
        this.attackDelay = 3;   // saniyede 1 saldırı
        this.cost = 75;        // maliyet
    }

    @Override
    public void update(float delta) {
        attackCooldown -= delta;
        if (attackCooldown <= 0) {
            dusman hedef = findNearestEnemy();
            if (hedef != null) {
                hedef.takeDamage(damage); // public metod üzerinden
                attackCooldown = attackDelay; // direkt attackDelay kullan
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y,texture.getWidth() * scale, texture.getHeight() *  scale);
    }

}
