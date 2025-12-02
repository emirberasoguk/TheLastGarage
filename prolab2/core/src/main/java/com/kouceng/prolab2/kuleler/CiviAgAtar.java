package com.kouceng.prolab2.kuleler;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.kouceng.prolab2.dusmanlar.*;

public class CiviAgAtar extends kule {

    public static float range = 150;

    public CiviAgAtar(float x, float y) {
        super(15, range, 2000, 70, x, y);
    }

    @Override
    public void onHit(dusman e, Array<dusman> allEnemies) {
        e.takeDamage(this.damage);
        e.slowDown(3000);
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
