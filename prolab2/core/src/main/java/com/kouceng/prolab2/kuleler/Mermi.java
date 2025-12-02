package com.kouceng.prolab2.kuleler;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.kouceng.prolab2.dusmanlar.dusman;

public class Mermi {
    private Vector2 position;
    private dusman target;
    private kule owner;
    private float speed = 500f; // Projectile speed
    private boolean active = true;

    public Mermi(float x, float y, dusman target, kule owner) {
        this.position = new Vector2(x, y);
        this.target = target;
        this.owner = owner;
    }

    public void update(float delta) {
        if (!active) return;

        // If target is dead or removed, destroy projectile
        if (target == null || target.isDead()) { // isDead logic might depend on specific implementation
             // For now, if target is just null, deactive.
             // Usually we want it to travel to the last position, but checking target validity is easier.
             active = false;
             return;
        }

        // Move towards target
        // We aim for the center of the target (assuming 30x30 size or similar, approx +15,+15)
        float targetX = target.getX() + 15;
        float targetY = target.getY() + 15;

        Vector2 direction = new Vector2(targetX - position.x, targetY - position.y).nor();
        position.mulAdd(direction, speed * delta);

        // Simple collision check (radius 5)
        if (Vector2.dst(position.x, position.y, targetX, targetY) < 10) {
            active = false; // Mark for removal
            // The actual hit logic will be handled by the GameScreen calling owner.onHit()
        }
    }

    public void render(ShapeRenderer sr) {
        if (owner instanceof AnahtarMakinesi) sr.setColor(Color.BLUE);
        else if (owner instanceof CiviAgAtar) sr.setColor(Color.YELLOW);
        else if (owner instanceof YagSizdirici) sr.setColor(Color.RED);
        else sr.setColor(Color.WHITE);

        sr.circle(position.x, position.y, 5);
    }

    public boolean isActive() {
        return active;
    }

    public boolean hasHit() {
        // Re-check distance or store a flag in update.
        // Since we set active=false on hit, we need to distinguish between "hit" and "fizzled".
        // Let's change the logic slightly.
        if (target == null) return false;
        float targetX = target.getX() + 15;
        float targetY = target.getY() + 15;
        return Vector2.dst(position.x, position.y, targetX, targetY) < 10;
    }

    public kule getOwner() {
        return owner;
    }

    public dusman getTarget() {
        return target;
    }
}
