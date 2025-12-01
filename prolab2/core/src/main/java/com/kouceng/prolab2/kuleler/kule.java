package com.kouceng.prolab2.kuleler;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

// Kuleler için temel soyut sınıf
public abstract class kule {

    protected int damage;       // Hasar
    protected float range;      // Menzil
    protected int fireRate;     // Atış hızı (ms)
    protected int cost;         // Maliyet
    protected Vector2 position; // Kulenin haritadaki yeri
    protected long lastAttackTime; // Son atış zamanı

    public kule(int damage, float range, int fireRate, int cost, float x, float y) {
        this.damage = damage;
        this.range = range;
        this.fireRate = fireRate;
        this.cost = cost;
        this.position = new Vector2(x, y);
        this.lastAttackTime = 0;
    }

    // Soyut Metotlar
    public abstract void attack(Enemy e, Array<Enemy> allEnemies);
    public abstract void draw(ShapeRenderer shapeRenderer);

    // Ortak Metotlar
    public boolean isInRange(Enemy e) {
        float dist = position.dst(e.getX(), e.getY());
        return dist <= range;
    }

    public boolean canAttack() {
        return System.currentTimeMillis() - lastAttackTime >= fireRate;
    }

    public int getCost() { return cost; }
    public float getX() { return position.x; }
    public float getY() { return position.y; }
}
