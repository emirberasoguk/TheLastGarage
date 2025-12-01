package com.prolab.game.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

public class YaglamaKulesi extends Tower {

    public YaglamaKulesi(float x, float y) {
        super(15, 150, 2000, 70, x, y);
    }

    @Override
    public void attack(Enemy e, Array<Enemy> allEnemies) {
        e.takeDamage(this.damage);
        e.slowDown(3000);
        this.lastAttackTime = System.currentTimeMillis();
    }

    @Override
    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.YELLOW);
        shapeRenderer.rect(position.x, position.y, 30, 30);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.circle(position.x + 15, position.y + 15, 10); // Center (x+15, y+15), radius 10
    }
}
