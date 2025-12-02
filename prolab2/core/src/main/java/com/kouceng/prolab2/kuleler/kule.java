package com.kouceng.prolab2.kuleler;

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

    public kule(int damage, float range, int fireRate, int cost, float x, float y) {
        this.damage = damage;
        this.range = range;
        this.fireRate = fireRate;
        this.cost = cost;
        this.position = new Vector2(x, y);
        this.lastAttackTime = 0;
    }

    // --- Soyut: Kule vurduğunda çalışacak (her kule kendi mantığıyla hasar verir)
    public abstract void onHit(dusman e, Array<dusman> allEnemies);

    // --- Ortak: Mermi oluştur
    public Mermi attack(dusman target) {
        this.lastAttackTime = System.currentTimeMillis();
        return new Mermi(position.x + 15, position.y + 15, target, this);
    }

    // --- Ortak: Menzil kontrolü
    public boolean isInRange(dusman e) {
        float dist = position.dst(e.getX(), e.getY());
        return dist <= range;
    }

    // --- Ortak: Atış süresi
    public boolean canAttack() {
        return System.currentTimeMillis() - lastAttackTime >= fireRate;
    }

    // Getterlar
    public int getCost() { return cost; }
    public float getX() { return position.x; }
    public float getY() { return position.y; }
    public float getRange() { return range; }
}
