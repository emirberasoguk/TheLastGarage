package com.kouceng.prolab2.dusmanlar;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GozcuUcagi extends dusman {

    public GozcuUcagi() {
        super(50, 75, 0, 15, true, 5);

        // --- DOĞRU: setTexture kullan ---
        setTexture(new Texture("gozcu_ucagi.png"));

        width = 60;
        height = 60;
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
    }
}
