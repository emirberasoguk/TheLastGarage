package com.kouceng.td.varliklar.kuleler;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kouceng.td.varliklar.dusman.dusman;

import java.util.List;

public abstract class kule {
    protected List<dusman> dusmanlar;

    protected float x, y;
    protected int damage;
    protected int attackDelay;
    protected int cost;
    float scale = 0.2f;

    public kule(float x, float y, List<dusman> dusmanlar) {
        this.x = x;
        this.y = y;
        this.dusmanlar = dusmanlar;
    }

    protected dusman findNearestEnemy() {
        for (dusman d : dusmanlar) {
            if (d.isAlive()) {
                return d; // listedeki ilk yaşayan düşman
            }
        }
        return null;
    }

    // Her kule için özelleştirilecek metodlar
    public abstract void update(float delta);  // saldırı mantığı vs.
    public abstract void render(SpriteBatch batch); // ekrana çizim

}
