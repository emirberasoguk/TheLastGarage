package com.kouceng.prolab2.kuleler;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.kouceng.prolab2.dusmanlar.*;

public class anahtarKulesi extends kule {

    public static float range = 150;

    public anahtarKulesi(float x, float y) {
        super(10, range, 1000, 50, x, y);
    }

    @Override
    public void attack(dusman e, Array<dusman> allEnemies) {
        int appliedDamage = this.damage;
        if (e.getArmor() > 0) {
            appliedDamage /= 2;
        }
        e.takeDamage(appliedDamage);
        this.lastAttackTime = System.currentTimeMillis();
    }

    @Override
    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(position.x, position.y, 30, 30);
    }

    public float getRange() {
        return range;
    }
}
