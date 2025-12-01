package com.kouceng.prolab2;

import com.badlogic.gdx.Game;
import com.kouceng.prolab2.GameScreen;

public class Prolab2 extends Game {
    @Override
    public void create() {
        setScreen(new GameScreen(this));
    }
}
