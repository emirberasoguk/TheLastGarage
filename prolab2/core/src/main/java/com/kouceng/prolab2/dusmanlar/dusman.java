package com.kouceng.prolab2.dusmanlar;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

// Düşmanlar için temel soyut sınıf (Abstract Class)
public abstract class dusman {

    protected int hp;
    protected int damage;
    protected int maxHp;
    protected float speed;
    protected double armor;
    protected int reward;
    protected boolean isFlying;
    protected Vector2 position;

    protected Array<Vector2> path;
    protected int currentTargetIndex = 0;
    protected boolean reachedEnd = false;

    protected float baseSpeed;
    protected long slowEndTime = 0;

    public dusman(int hp, float speed, double armor, int reward, boolean isFlying, int damage) {
        this.hp = hp;
        this.maxHp = hp;
        this.damage = damage;
        this.speed = speed;
        this.baseSpeed = speed;
        this.armor = armor;
        this.reward = reward;
        this.isFlying = isFlying;
        this.position = new Vector2(0, 0);
    }

    public void slowDown(long durationMs) {
        this.slowEndTime = System.currentTimeMillis() + durationMs;
    }

    public void setPath(Array<Vector2> path) {
        this.path = path;
        if (path != null && path.size > 0) {
            this.position.set(path.get(0));
            this.currentTargetIndex = 1;
        }
    }

    public int getDamage() {
        return damage;
    }

    public int getHp() {
        return hp;
    }

    public void move(float delta) {
        if (path == null || path.size == 0 || reachedEnd) return;

        float currentSpeed = baseSpeed;
        if (System.currentTimeMillis() < slowEndTime) {
            currentSpeed = baseSpeed / 2;
        }

        // UÇANLAR DA ARTIK NORMAL GİBİ PATH TAKİP EDECEK
        if (currentTargetIndex >= path.size) {
            reachedEnd = true;
            return;
        }

        Vector2 target = path.get(currentTargetIndex);

        Vector2 direction = new Vector2(target).sub(position);
        float distance = direction.len();
        float moveAmount = currentSpeed * delta;

        if (distance <= moveAmount) {
            position.set(target);
            currentTargetIndex++;
        } else {
            direction.nor();
            position.add(direction.scl(moveAmount));
        }
    }

    public boolean isSlowed() {
        return System.currentTimeMillis() < slowEndTime;
    }

    public abstract void draw(ShapeRenderer shapeRenderer);

    public void takeDamage(int damage) {
        int netDamage = (int)(damage * (1.0 - (armor / (armor + 100.0))));
        if (netDamage < 1) netDamage = 1;
        this.hp -= netDamage;
    }

    public boolean isDead() {
        return hp <= 0;
    }

    public boolean hasReachedEnd() {
        return reachedEnd;
    }

    public float getX() { return position.x; }
    public float getY() { return position.y; }
    public int getReward() { return reward; }
    public boolean isFlying() { return isFlying; }
    public double getArmor() { return armor; }
}
