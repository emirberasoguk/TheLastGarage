package com.kouceng.prolab2.kuleler;

import com.badlogic.gdx.math.Vector2;

public class TowerSpot {
    public Vector2 pos;
    public boolean occupied = false;

    public TowerSpot(float x, float y) {
        pos = new Vector2(x, y);
    }
}
