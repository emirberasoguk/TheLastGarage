package com.kouceng.prolab2.kuleler;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.kouceng.prolab2.dusmanlar.*;
import com.kouceng.prolab2.log.CombatLog;

public class YagSizdirici extends kule {

    public static float range = 200;

    private static Texture texture;

    public YagSizdirici(float x, float y) {
        super(20, range, 3000, 75, x, y);

        if (texture == null)
            texture = new Texture("yag.png");
    }

    @Override
    public void onHit(dusman target, Array<dusman> allEnemies) {

        if (target.isFlyingEnemy()) return;

        float aoeRadius = 150f;

        CombatLog.aoeCenter(
            this.getClass().getSimpleName() + "-" + this.getId(),
            target.getClass().getSimpleName() + "-" + target.getID()
        );

        for (dusman e : allEnemies) {
            if (e.isFlyingEnemy()) continue;

            float dist = Vector2.dst(e.getX(), e.getY(), target.getX(), target.getY());
            if (dist <= aoeRadius) {

                int before = e.getHp();

                // ZIRH HESABI TAKE DAMAGE içinde!
                e.takeDamage(this.damage);

                int net = before - e.getHp();

                CombatLog.aoeDamage(
                    e.getClass().getSimpleName() + "-" + e.getID(),
                    net
                );
            }
        }
    }


    @Override
    public void render(SpriteBatch batch) {
        float sizeX = 120;
        float sizeY = 150;

        batch.draw(texture,
            position.x - sizeX / 2,
            position.y - sizeY / 2,
            sizeX,
            sizeY);
    }

    public static void disposeTextures() {
        if (texture != null)
            texture.dispose();
    }

    public float getRange() {
        return range;
    }
}
