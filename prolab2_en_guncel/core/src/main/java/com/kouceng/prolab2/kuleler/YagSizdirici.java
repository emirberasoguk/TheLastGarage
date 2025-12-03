package com.kouceng.prolab2.kuleler;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.kouceng.prolab2.dusmanlar.*;

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

        float aoeRadius = 100;

        float tx = target.getX() + 10;
        float ty = target.getY() + 10;

        for (dusman e : allEnemies) {
            if (e.isFlyingEnemy()) continue;

            float ex = e.getX() + 10;
            float ey = e.getY() + 10;

            float dist = (float)Math.hypot(ex - tx, ey - ty);

            if (dist <= aoeRadius) {

                double armor = e.getArmor();
                int rawDamage = this.damage;

                double factor = 1.0 - (armor / (armor + 100.0));
                int finalDamage = (int)(rawDamage * factor);
                if (finalDamage < 1) finalDamage = 1;

                e.takeDamage(finalDamage);
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
