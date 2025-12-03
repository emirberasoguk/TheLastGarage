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

        // Tek sefer yüklenir — doğru kullanım
        if (texture == null)
            texture = new Texture("civi.png");
    }

    @Override
    public void onHit(dusman e, Array<dusman> allEnemies) {

        double armor = e.getArmor();

        double reductionFactor = 1.0 - (armor / (armor + 100.0));
        int finalDamage = (int)(damage * reductionFactor);
        if (finalDamage < 1) finalDamage = 1;

        e.takeDamage(finalDamage);

        // Yavaşlatma efekti: 3 saniye
        e.slowDown(3000);
    }

    @Override
    public void render(SpriteBatch batch) {

        float sizeX = 120;
        float sizeY = 110;

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
