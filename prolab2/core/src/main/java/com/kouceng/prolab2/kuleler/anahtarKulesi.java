package com.kouceng.prolab2.kuleler;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

public class anahtarKulesi extends kule {

    public anahtarKulesi(float x, float y) {
        super(10, 150, 1000, 50, x, y);
    }

    @Override
    public void attack(Enemy e, Array<Enemy> allEnemies) {
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
}
