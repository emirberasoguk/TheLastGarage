package com.kouceng.prolab2.kuleler;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.kouceng.prolab2.dusmanlar.*;

public class AnahtarMakinesi extends kule {

    public static float range = 150;
    private static Texture texture;

    public AnahtarMakinesi(float x, float y) {
        super(10, range, 1000, 50, x, y);

        if (texture == null)
            texture = new Texture("anahtar.png");
    }

    @Override
    public void onHit(dusman e, Array<dusman> allEnemies) {

        double armor = e.getArmor();
        int rawDamage = this.damage;
        double red = 1.0 - (armor / (armor + 100.0));
        int finalDamage = (int)(rawDamage * red);
        if (finalDamage < 1) finalDamage = 1;

        e.takeDamage(finalDamage);
    }

    @Override
    public void draw(ShapeRenderer shapeRenderer) {
        // Rendered via SpriteBatch in GameScreen
    }

    public void render(SpriteBatch batch) {
        float size_x = 120;
        float size_y = 110;
        batch.draw(texture, position.x - size_x/2, position.y - size_y/2, size_x, size_y);
    }

    public static void disposeTextures() {
        if (texture != null) texture.dispose();
    }

    public float getRange() { return range; }
}
