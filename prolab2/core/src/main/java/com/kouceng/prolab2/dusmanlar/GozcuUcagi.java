package com.kouceng.prolab2.dusmanlar;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class GozcuUcagi extends dusman {

    public GozcuUcagi() {
        super(50, 75, 0, 15, true, 5);
    }

    @Override
    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.triangle(position.x, position.y,
            position.x + 20, position.y,
            position.x + 10, position.y + 20);

        // HP Bar
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(position.x, position.y - 5, 20, 3);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(position.x, position.y - 5, 20 * ((float)hp/maxHp), 3);
    }
}
