package com.kouceng.prolab2.kuleler;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.kouceng.prolab2.dusmanlar.*;

public class CiviAgAtar extends kule {

    public static float range = 150;

    private static Texture texture;

    public CiviAgAtar(float x, float y) {
        super(15, range, 2000, 70, x, y);


        if (texture == null)
            texture = new Texture("civi.png");
    }

    @Override
    public void onHit(dusman e, Array<dusman> allEnemies) {

        e.takeDamage(this.damage);

        // Yavaşlatma efekti: 3 saniye
        e.applySlow(50,3);
    }

    @Override
    public void render(SpriteBatch batch) {

        float sizeX = 120;
        float sizeY = 150;

        batch.draw(texture,
            position.x - sizeX/2,
            position.y - sizeY/2,
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
