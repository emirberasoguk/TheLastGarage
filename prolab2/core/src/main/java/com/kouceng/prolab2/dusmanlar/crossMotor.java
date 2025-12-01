
package com.kouceng.prolab2.dusmanlar;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class crossMotor extends dusman {

    public crossMotor() {
        super(50, 50, 0, 10, false);
    }

    @Override
    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.circle(position.x + 10, position.y + 10, 10); // Center +10,+10, radius 10 (diameter 20)

        // HP Bar
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(position.x, position.y - 5, 20, 3);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(position.x, position.y - 5, 20 * ((float)hp/maxHp), 3);
    }
}
