package com.kouceng.prolab2.kuleler;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.kouceng.prolab2.dusmanlar.*;

public class AnahtarMakinesi extends kule {

    public static float range = 150;
    private static Texture texture;

    public AnahtarMakinesi(float x, float y) {
        super(10, range, 1000, 50, x, y);

        // PNG sadece 1 kez yüklenir
        if (texture == null)
            texture = new Texture("anahtar.png");
    }

    @Override
    public void onHit(dusman e, Array<dusman> allEnemies) {

        double armor = e.getArmor();
        double reduction = 1.0 - (armor / (armor + 100.0));

        int finalDamage = (int)(damage * reduction);
        if (finalDamage < 1) finalDamage = 1;

        e.takeDamage(finalDamage);
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

    public float getRange() { return range; }
}
