package com.kouceng.prolab2.kuleler;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.kouceng.prolab2.dusmanlar.*;

public class YagSizdirici extends kule {

    public static float range = 200;

    public YagSizdirici(float x, float y) {
        super(20, range, 3000, 75, x, y);
    }

    @Override
    public void onHit(dusman target, Array<dusman> allEnemies) {
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
    }

    public float getRange() {
        return range;
    }

    @Override
    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(1, 1, 1, 1);

        float size = 30;         // Kule boyutu
        float half = size / 2f;  // Merkez için yarısı

        shapeRenderer.rect(
            position.x - half,
            position.y - half,
            size,
            size
        );
    }

}
