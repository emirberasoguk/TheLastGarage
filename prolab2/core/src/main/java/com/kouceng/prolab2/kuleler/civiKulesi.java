package com.kouceng.prolab2.kuleler;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.kouceng.prolab2.dusmanlar.*;

public class civiKulesi extends kule {

    public static float range = 150;

    public civiKulesi(float x, float y) {
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
        shapeRenderer.setColor(Color.YELLOW);
        shapeRenderer.rect(position.x, position.y, 30, 30);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.circle(position.x + 15, position.y + 15, 10); // Center (x+15, y+15), radius 10
    }
}
