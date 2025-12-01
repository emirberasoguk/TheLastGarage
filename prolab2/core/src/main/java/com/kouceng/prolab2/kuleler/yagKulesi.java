package com.kouceng.prolab2.kuleler;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.kouceng.prolab2.dusmanlar.dusman;

public class yagKulesi extends kule {

    public yagKulesi(float x, float y) {
        super(20, 200, 3000, 75, x, y);
    }

    @Override
    public void attack(dusman target, Array<dusman> allEnemies) {
        if (target.isFlying()) return;

        float aoeRadius = 50;
        float tx = target.getX() + 10; // Center approx
        float ty = target.getY() + 10;

        for (dusman e : allEnemies) {
            if (e.isFlying()) continue;

            float ex = e.getX() + 10;
            float ey = e.getY() + 10;
            float dist = (float)Math.hypot(ex - tx, ey - ty);

            if (dist <= aoeRadius) {
                e.takeDamage(this.damage);
            }
        }
        this.lastAttackTime = System.currentTimeMillis();
    }

    @Override
    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(position.x, position.y, 30, 30);
        // Border support in ShapeRenderer requires switching to Line type, usually handled in main loop
        // For simplicity, we just draw filled rect here.
    }
}
