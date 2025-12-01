package com.prolab.game.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.Random;

public class ZirhliKamyon extends Enemy {

    public ZirhliKamyon() {
        super(75, 25, 0, 20, false);
        Random rand = new Random();
        this.armor = 50 + rand.nextInt(51);
    }

    @Override
    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(new Color(139/255f, 69/255f, 19/255f, 1f)); // SaddleBrown
        shapeRenderer.rect(position.x, position.y, 30, 20);
        
        // HP Bar
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(position.x, position.y - 5, 30, 3);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(position.x, position.y - 5, 30 * ((float)hp/maxHp), 3);
    }
}
