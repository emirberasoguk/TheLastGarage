package com.prolab.game.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.prolab.game.MyGdxGame;

public class DesktopLauncher {
    public static void main (String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Sanayi Hurdalığı - LibGDX");
        config.setWindowedMode(800, 600);
        config.setForegroundFPS(60);
        new Lwjgl3Application(new MyGdxGame(), config);
    }
}
