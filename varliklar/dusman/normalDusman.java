package com.kouceng.td.varliklar.dusman;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;

public class normalDusman extends dusman{
    private Texture texture;

    public normalDusman(float x, float y) {
        this.texture = new Texture("iskelet.png");
        this.x = x;
        this.y = y;
        this.scale = 0.05f;
        this.width = texture.getWidth() * this.scale;
        this.height = texture.getHeight() * this.scale;
        this.speed = 50;
        this.health = 50;
        this.damage = 5;
        this.reward = 10;
        this.armour = 0;
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


    // istersen getter ve setterlar ekleyebilirsin
}
