package com.kouceng.prolab2.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.kouceng.prolab2.Prolab2;

public class MainMenuScreen implements Screen {

    final Prolab2 game;
    OrthographicCamera camera;

    // Görseller
    private Texture background;
    private Texture logo;

    private Texture btnPlay, btnPlayHover;
    private Texture btnExit, btnExitHover;

    // Buton konumları
    private float btnWidth = 300;
    private float btnHeight = 100;

    private float playX;
    private float playY = 350;

    private float exitX;
    private float exitY = 250;

    // Fade-in animasyonu
    private float fadeAlpha = 0f;
    private float fadeSpeed = 0.015f;

    public MainMenuScreen(final Prolab2 game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);

        // Görseller
        background = new Texture("menu_bg.png");
        logo = new Texture("logo.png");

        btnPlay = new Texture("baslat.png");
        btnPlayHover = new Texture("baslat_hover.png");

        btnExit = new Texture("cikis.png");
        btnExitHover = new Texture("cikis_hover.png");

        // Ortalamak için X konumu
        playX = (1280 - btnWidth) / 2;
        exitX = (1280 - btnWidth) / 2;
    }

    @Override
    public void render(float delta) {

        // Fade-in artır
        if (fadeAlpha < 1f) fadeAlpha += fadeSpeed;

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        // Mouse pozisyonu
        float mx = Gdx.input.getX();
        float my = 720 - Gdx.input.getY();

        game.batch.begin();

        // ================= SABİT ARKA PLAN =================
        game.batch.setColor(1, 1, 1, fadeAlpha);
        game.batch.draw(background, 0, 0, 1280, 720);

        // ================= LOGO =================
        if (logo != null) {
            float logoWidth = 700;
            float logoHeight = 200;
            float logoX = (1280 - logoWidth) / 2;
            float logoY = 450;
            game.batch.draw(logo, logoX, logoY, logoWidth, logoHeight);
        }

        // ================= BUTONLAR =================

        // OYUNU BAŞLAT ---------------------------------------
        boolean hoverPlay = mx > playX && mx < playX + btnWidth &&
            my > playY && my < playY + btnHeight;

        float scalePlay = hoverPlay ? 1.05f : 1f;
        float scaledWPlay = btnWidth * scalePlay;
        float scaledHPlay = btnHeight * scalePlay;
        float scaledXPlay = playX - (scaledWPlay - btnWidth) / 2;
        float scaledYPlay = playY - (scaledHPlay - btnHeight) / 2;

        game.batch.draw(
            hoverPlay ? btnPlayHover : btnPlay,
            scaledXPlay, scaledYPlay,
            scaledWPlay, scaledHPlay
        );

        // ÇIKIŞ ----------------------------------------------
        boolean hoverExit = mx > exitX && mx < exitX + btnWidth &&
            my > exitY && my < exitY + btnHeight;

        float scaleExit = hoverExit ? 1.05f : 1f;
        float scaledWExit = btnWidth * scaleExit;
        float scaledHExit = btnHeight * scaleExit;
        float scaledXExit = exitX - (scaledWExit - btnWidth) / 2;
        float scaledYExit = exitY - (scaledHExit - btnHeight) / 2;

        game.batch.draw(
            hoverExit ? btnExitHover : btnExit,
            scaledXExit, scaledYExit,
            scaledWExit, scaledHExit
        );

        game.batch.end();

        // ================= TIKLAMA KONTROL =================
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {

            if (hoverPlay) {
                game.setScreen(new GameScreen(game));
                dispose();
            }

            if (hoverExit) {
                Gdx.app.exit();
            }
        }
    }

    @Override
    public void dispose() {
        background.dispose();
        if (logo != null) logo.dispose();
        btnPlay.dispose();
        btnPlayHover.dispose();
        btnExit.dispose();
        btnExitHover.dispose();
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
