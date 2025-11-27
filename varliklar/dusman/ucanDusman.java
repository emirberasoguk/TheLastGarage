package com.kouceng.td.varliklar.dusman;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ucanDusman extends dusman{
    private Texture texture;

    public ucanDusman(float x, float y) {
        this.x = x;
        this.y = y;
        this.speed = 75;
        this.health = 50;
        this.damage = 5;
        this.reward = 15;
        this.armour = 0;
        this.texture = new Texture("ucanDusman.png");
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
