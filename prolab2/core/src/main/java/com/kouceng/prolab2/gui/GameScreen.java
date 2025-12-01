package com.kouceng.prolab2.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.kouceng.prolab2.Prolab2;

// Enemies
import com.kouceng.prolab2.dusmanlar.dusman;
import com.kouceng.prolab2.dusmanlar.crossMotor;
import com.kouceng.prolab2.dusmanlar.ucak;
import com.kouceng.prolab2.dusmanlar.zirhliKamyon;

// Towers
import com.kouceng.prolab2.kuleler.kule;
import com.kouceng.prolab2.kuleler.civiKulesi;
import com.kouceng.prolab2.kuleler.yagKulesi;
import com.kouceng.prolab2.kuleler.anahtarKulesi;

import java.util.Iterator;
import java.util.Random;

public class GameScreen implements Screen {

    final Prolab2 game;
    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;

    private Texture mapTexture;

    // Game state
    private int scrap = 200;
    private int garageHp = 100;
    private int wave = 0;
    private boolean isGameOver = false;
    private boolean isWaveActive = false;

    // Entities
    private Array<dusman> enemies;
    private Array<kule> towers;
    private Array<Vector2> path;
    private Array<Rectangle> pathRects;

    // Ghost Tower
    private String selectedTowerType = null;
    private float towerGhostRange = 100;

    // Waves
    private Array<dusman> spawnQueue;
    private long lastSpawnTime = 0;
    private final int SPAWN_INTERVAL = 1200;

    public GameScreen(final Prolab2 game) {
        this.game = game;

        enemies = new Array<>();
        towers = new Array<>();
        path = new Array<>();
        pathRects = new Array<>();
        spawnQueue = new Array<>();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);
        shapeRenderer = new ShapeRenderer();

        mapTexture = new Texture("map.png");

        initPath();
    }

    // ==========================================
    //       PATH — MAP’e GÖRE ÇIKARILDI
    // ==========================================
    private void initPath() {

        path.add(new Vector2(-20, 340));     // giriş
        path.add(new Vector2(350, 340));
        path.add(new Vector2(350, 560));
        path.add(new Vector2(655, 560));
        path.add(new Vector2(655, 180));
        path.add(new Vector2(960, 180));
        path.add(new Vector2(960, 340));
        path.add(new Vector2(1020, 340));    // BASE

        // Collision Rectangles
        for (int i = 0; i < path.size - 1; i++) {

            Vector2 a = path.get(i);
            Vector2 b = path.get(i + 1);

            float minX = Math.min(a.x, b.x);
            float minY = Math.min(a.y, b.y);
            float width = Math.abs(a.x - b.x);
            float height = Math.abs(a.y - b.y);

            if (width == 0) width = 40;
            if (height == 0) height = 40;

            pathRects.add(new Rectangle(minX - 28, minY - 28, width + 56, height + 56));
        }
    }

    // ==========================================
    //                RENDER
    // ==========================================
    @Override
    public void render(float delta) {

        handleInput();
        update(delta);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(mapTexture, 0, 0, 1280, 720);
        game.batch.end();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Towers
        for (kule t : towers) t.draw(shapeRenderer);

        // Enemies
        for (dusman e : enemies) e.draw(shapeRenderer);

        drawGhostTower();

        shapeRenderer.end();

        drawUI();
    }

    // ==========================================
    //          GHOST TOWER
    // ==========================================
    private void drawGhostTower() {
        if (selectedTowerType == null) return;

        Vector3 m = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(m);

        // range
        shapeRenderer.setColor(0, 0.4f, 1f, 0.20f);
        shapeRenderer.circle(m.x, m.y, towerGhostRange);

        // transparent tower
        if (selectedTowerType.equals("Civi")) shapeRenderer.setColor(0.7f, 0.7f, 0.7f, 0.6f);
        if (selectedTowerType.equals("Anahtar")) shapeRenderer.setColor(1f, 0.5f, 0f, 0.6f);
        if (selectedTowerType.equals("Yag")) shapeRenderer.setColor(0f, 0f, 0f, 0.6f);

        shapeRenderer.rect(m.x - 15, m.y - 15, 30, 30);
    }

    // ==========================================
    //               INPUT
    // ==========================================
    private void handleInput() {
        if (isGameOver) return;

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) startNextWave();

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            selectedTowerType = "Civi";
            towerGhostRange = civiKulesi.range;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            selectedTowerType = "Anahtar";
            towerGhostRange = anahtarKulesi.range;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            selectedTowerType = "Yag";
            towerGhostRange = yagKulesi.range;
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && selectedTowerType != null) {
            Vector3 m = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(m);

            if (!isOnPath(m.x, m.y)) {
                placeTower(m.x, m.y);
            }
        }
    }

    private boolean isOnPath(float x, float y) {
        for (Rectangle r : pathRects)
            if (r.contains(x, y)) return true;

        return false;
    }

    private void placeTower(float x, float y) {
        kule t = null;

        if (selectedTowerType.equals("Civi") && scrap >= 50)
            t = new civiKulesi(x, y);

        if (selectedTowerType.equals("Anahtar") && scrap >= 75)
            t = new anahtarKulesi(x, y);

        if (selectedTowerType.equals("Yag") && scrap >= 70)
            t = new yagKulesi(x, y);

        if (t != null) {
            scrap -= t.getCost();
            towers.add(t);
            selectedTowerType = null;
        }
    }

    // ==========================================
    //               GAME UPDATE
    // ==========================================
    private void update(float delta) {

        if (isGameOver) return;

        // Enemy spawning
        if (isWaveActive && spawnQueue.size > 0) {
            if (System.currentTimeMillis() - lastSpawnTime > SPAWN_INTERVAL) {
                dusman e = spawnQueue.removeIndex(0);
                e.setPath(path);
                enemies.add(e);
                lastSpawnTime = System.currentTimeMillis();
            }
        }

        // Enemy movement & reaching base
        Iterator<dusman> it = enemies.iterator();
        while (it.hasNext()) {
            dusman e = it.next();
            e.move(delta);

            if (e.hasReachedEnd()) {
                garageHp -= e.getDamage();
                it.remove();

                if (garageHp <= 0) {
                    garageHp = 0;
                    isGameOver = true;
                }
            }
        }

        // Tower attack
        for (kule t : towers) {
            if (!t.canAttack()) continue;

            dusman target = null;
            for (dusman e : enemies) {
                if (t.isInRange(e)) {
                    target = e;
                    break;
                }
            }

            if (target != null) {
                t.attack(target, enemies);
                if (target.isDead()) {
                    scrap += target.getReward();
                    enemies.removeValue(target, true);
                }
            }
        }

        // End wave
        if (isWaveActive && spawnQueue.size == 0 && enemies.size == 0) {
            isWaveActive = false;
        }
    }

    private void startNextWave() {
        if (isWaveActive || isGameOver) return;

        wave++;
        generateWave(wave);
        isWaveActive = true;
    }

    private void generateWave(int waveNum) {
        spawnQueue.clear();
        Random r = new Random();

        int count = 5 + r.nextInt(6);

        for (int i = 0; i < count; i++) {
            int t = r.nextInt(3);

            if (t == 0) spawnQueue.add(new crossMotor());
            if (t == 1) spawnQueue.add(new zirhliKamyon());
            if (t == 2) spawnQueue.add(new ucak());
        }
    }

    // ==========================================
    //                   UI
    // ==========================================
    private void drawUI() {
        game.batch.begin();

        float w = 1280, h = 720;

        game.font.setColor(Color.BLACK);
        game.font.draw(game.batch, "Can: " + garageHp, 20, h - 20);
        game.font.draw(game.batch, "Hurda: " + scrap, 150, h - 20);
        game.font.draw(game.batch, "Dalga: " + wave, 300, h - 20);

        game.font.draw(game.batch,
            "[1] Civi | [2] Anahtar | [3] Yag | [SPACE] Dalga",
            20, 40);

        if (selectedTowerType != null)
            game.font.draw(game.batch, "Secili: " + selectedTowerType, w - 250, h - 20);

        if (isGameOver) {
            game.font.getData().setScale(2);
            game.font.draw(game.batch,
                garageHp > 0 ? "KAZANDIN!" : "KAYBETTIN!",
                w / 2f - 120, h / 2f);
            game.font.getData().setScale(1);
        }

        game.batch.end();
    }

    @Override public void dispose() {
        shapeRenderer.dispose();
        mapTexture.dispose();
    }
    @Override public void show() {}
    @Override public void resize(int w, int h) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
