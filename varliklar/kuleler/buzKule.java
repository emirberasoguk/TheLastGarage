package com.kouceng.td.varliklar.kuleler;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.kouceng.td.varliklar.dusman.dusman; // düşmanları kontrol etmek için

import java.util.ArrayList;
import java.util.List;


public class buzKule extends kule {
    private Texture texture;
    private float attackCooldown = 0; // saldırı bekleme süresi

    public buzKule(float x, float y, List<dusman> dusmanlar) {
        super(x, y, dusmanlar);

        this.texture = new Texture("buz_kule.png"); // resim dosyası
        this.damage = 20;       // hasar
        this.attackDelay = 1;   // saniyede 1 saldırı
        this.cost = 100;        // maliyet
    }

    @Override
    public void update(float delta) {
        attackCooldown -= delta;

        if (attackCooldown <= 0) {
            dusman hedef = findNearestEnemy();
            if (hedef != null) {
                hedef.takeDamage(damage);
                hedef.applySlow(0.5f, 3f); // %50 yavaşlat, 3 saniye süresince
                attackCooldown = attackDelay;
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y,texture.getWidth() * scale, texture.getHeight() *  scale);
    }

}
