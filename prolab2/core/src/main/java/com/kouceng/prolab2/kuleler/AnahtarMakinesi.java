package com.kouceng.prolab2.kuleler;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.kouceng.prolab2.dusmanlar.*;

public class AnahtarMakinesi extends kule {

    public static float range = 150;

    public AnahtarMakinesi(float x, float y) {
        super(10, range, 1000, 50, x, y);
    }

    @Override
    public void onHit(dusman e, Array<dusman> allEnemies) {
        int appliedDamage = this.damage;
        if (e.getArmor() > 0) {
            appliedDamage /= 2;
        }
        e.takeDamage(appliedDamage);
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


    public float getRange() {
        return range;
    }
}
