package com.kouceng.prolab2;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kouceng.prolab2.gui.GameScreen;
import com.kouceng.prolab2.gui.MainMenuScreen;

public class Prolab2 extends Game {

    public SpriteBatch batch;
    public BitmapFont font;

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();

        setScreen(new MainMenuScreen(this));
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }


}
