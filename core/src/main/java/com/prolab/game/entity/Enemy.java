package com.prolab.game.entity;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

// Düşmanlar için temel soyut sınıf (Abstract Class)
public abstract class Enemy {
    
    // Ortak özellikler
    protected int hp;           // Can
    protected int maxHp;        // Maksimum can
    protected float speed;      // Hız
    protected double armor;     // Zırh
    protected int reward;       // Ödül
    protected boolean isFlying; // Uçuyor mu?
    protected Vector2 position; // Haritadaki konumu

    protected Array<Vector2> path; // İzleyeceği yol
    protected int currentTargetIndex = 0; // Yoldaki hangi noktaya gidiyor
    protected boolean reachedEnd = false; // Sona ulaştı mı
    
    protected float baseSpeed;
    protected long slowEndTime = 0;

    // Kurucu Metot (Constructor)
    public Enemy(int hp, float speed, double armor, int reward, boolean isFlying) {
        this.hp = hp;
        this.maxHp = hp;
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

    // Yolu ata
    public void setPath(Array<Vector2> path) {
        this.path = path;
        if (path != null && path.size > 0) {
            this.position.set(path.get(0));
            this.currentTargetIndex = 1; // İlk noktadan başla, ikinciye git
        }
    }

    // Hareketi işle - LibGDX delta time kullanır
    public void move(float delta) {
        if (path == null || path.size == 0 || reachedEnd) return;
        
        // Yavaşlatma kontrolü
        float currentSpeed = baseSpeed;
        if (System.currentTimeMillis() < slowEndTime) {
            currentSpeed = baseSpeed / 2;
        }

        Vector2 target;
        if (isFlying) {
            // Uçanlar doğrudan son noktaya gider
            target = path.get(path.size - 1);
        } else {
            // Yerdekiler sırayla gider
            if (currentTargetIndex >= path.size) {
                reachedEnd = true;
                return;
            }
            target = path.get(currentTargetIndex);
        }

        // LibGDX Vector2 metodları ile hareket
        Vector2 direction = new Vector2(target).sub(position);
        float distance = direction.len();
        float moveAmount = currentSpeed * delta; // speed pixels per second

        if (distance <= moveAmount) {
            position.set(target);
            if (!isFlying) currentTargetIndex++;
            else reachedEnd = true;
        } else {
            direction.nor(); // Normalize et
            position.add(direction.scl(moveAmount));
        }
    }
    
    public boolean isSlowed() { return System.currentTimeMillis() < slowEndTime; }
    
    // Çizim metodu - ShapeRenderer kullanır
    public abstract void draw(ShapeRenderer shapeRenderer); 

    // Somut Metotlar
    public void takeDamage(int damage) {
        // Zırh formülü: Net_Hasar = Hasar * (1 - (Zırh / (Zırh + 100)))
        int netDamage = (int) (damage * (1.0 - (armor / (armor + 100.0))));
        if (netDamage < 1) netDamage = 1; // En az 1 hasar
        this.hp -= netDamage;
    }

    public boolean isDead() {
        return hp <= 0;
    }
    
    public boolean hasReachedEnd() { return reachedEnd; }

    // Getter ve Setter'lar
    public float getX() { return position.x; }
    public float getY() { return position.y; }
    public int getReward() { return reward; }
    public boolean isFlying() { return isFlying; }
    public double getArmor() { return armor; }
}
