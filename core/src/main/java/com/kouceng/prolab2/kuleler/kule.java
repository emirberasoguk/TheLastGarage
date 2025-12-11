package com.kouceng.prolab2.kuleler;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.kouceng.prolab2.dusmanlar.*;

public abstract class kule {

    protected int damage;           // Hasar
    protected float range;          // Menzil
    protected int fireRate;         // Atış hızı (ms)
    protected int cost;             // Maliyet
    protected Vector2 position;     // Kulenin haritadaki yeri
    protected long lastAttackTime;  // Son atış zamanı
    protected String id;
    private static int counter = 1;

    public kule(int damage, float range, int fireRate, int cost, float x, float y) {
        this.id = String.format("ID%03d", counter++);
        this.damage = damage;
        this.range = range;
        this.fireRate = fireRate;
        this.cost = cost;
        this.position = new Vector2(x, y);
        this.lastAttackTime = 0;
    }

    // render
    public abstract void render(SpriteBatch batch);

    // isabet
    public abstract void onHit(dusman e, Array<dusman> allEnemies);

    // mermi olsturma
    public Mermi attack(dusman target) {
        this.lastAttackTime = System.currentTimeMillis();
        return new Mermi(position.x + 15, position.y + 15, target, this);
    }

    // menzil kontrol
    public boolean isInRange(dusman e) {
        float dist = position.dst(e.getX(), e.getY());
        return dist <= range;
    }

    // süre hesabı
    public boolean canAttack() {
        return System.currentTimeMillis() - lastAttackTime >= fireRate;
    }

    // getter
    public String getId() {
        return id;
    }

    public int getDamage() {
        return damage;
    }

    public int getCost() { return cost; }
    public float getX() { return position.x; }
    public float getY() { return position.y; }
    public float getRange() { return range; }
}
