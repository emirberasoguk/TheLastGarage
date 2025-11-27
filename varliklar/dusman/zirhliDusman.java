package com.kouceng.td.varliklar.dusman;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;

public class zirhliDusman extends dusman{
    private Texture texture;

    public zirhliDusman(float x, float y) {
        this.x = x;
        this.y = y;
        this.speed = 25;
        this.health = 75;
        this.damage = 10;
        this.reward = 20;
        this.armour = 50;
        this.texture = new Texture("zirhli_iskelet.png");
    }
    @Override
    public void update(float delta) {
        // Basit bir örnek: düşmanı sola doğru hareket ettir
        x += speed * delta;

        // buraya başka mantıklar (yolu takip etme vs.) eklenebilir
    }

    @Override
    public void render(SpriteBatch batch) {
        // Renk resetini her ihtimale karşı başta yap
        batch.setColor(1f, 1f, 1f, 1f);

        if (isSlowed()) {
            batch.setColor(0.5f, 0.8f, 1f, 1f); // mavi ton
        }

        batch.draw(texture, x, y, width, height);

        // Sonra tekrar sıfırla
        batch.setColor(1f, 1f, 1f, 1f);
    }

}

