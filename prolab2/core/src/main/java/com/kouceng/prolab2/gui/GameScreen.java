package com.kouceng.prolab2.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.kouceng.prolab2.Prolab2;

// Düşmanlar
import com.kouceng.prolab2.dusmanlar.dusman;
import com.kouceng.prolab2.dusmanlar.crossMotor;
import com.kouceng.prolab2.dusmanlar.ucak;
import com.kouceng.prolab2.dusmanlar.zirhliKamyon;

// Kuleler
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

    // Game State
    private int scrap = 200;
    private int garageHp = 100;
    private int wave = 0;
    private boolean isGameOver = false;
    private boolean isWaveActive = false;

    // Entities
    private Array<dusman> enemies;
    private Array<kule> towers;
    private Array<Vector2> path;

    // Wave Management
    private Array<dusman> spawnQueue;
    private long lastSpawnTime = 0;
    private final int SPAWN_INTERVAL = 1200;

    // Input
    private String selectedTowerType = null; // "Civi", "Anahtar", "Yag"

    public GameScreen(final Prolab2 game) {
        this.game = game;

        this.enemies = new Array<>();
        this.towers = new Array<>();
        this.path = new Array<>();
        this.spawnQueue = new Array<>();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);
        shapeRenderer = new ShapeRenderer();

        initPath();
    }

    private void initPath() {
        path.add(new Vector2(0, 300));
        path.add(new Vector2(200, 300));
        path.add(new Vector2(200, 500));
        path.add(new Vector2(500, 500));
        path.add(new Vector2(500, 200));
        path.add(new Vector2(700, 200));
        path.add(new Vector2(700, 300));
    }

    @Override
    public void render(float delta) {

        handleInput();
        update(delta);

        Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);
        game.batch.setProjectionMatrix(camera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // ➤ Path çizimi
        shapeRenderer.setColor(Color.DARK_GRAY);
        if (path.size > 0) {
            Vector2 p1 = path.get(0);
            for (int i = 1; i < path.size; i++) {
                Vector2 p2 = path.get(i);
                shapeRenderer.rectLine(p1, p2, 5);
                p1 = p2;
            }
        }

        // ➤ Garaj
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(700, 280, 50, 40);

        // ➤ Kuleler
        for (kule t : towers) {
            t.draw(shapeRenderer);
        }

        // ➤ Düşmanlar
        for (dusman e : enemies) {
            e.draw(shapeRenderer);
        }

        shapeRenderer.end();

        // ➤ UI
        game.batch.begin();
        game.font.setColor(Color.BLACK);
        game.font.draw(game.batch, "Can: " + garageHp, 10, 580);
        game.font.draw(game.batch, "Hurda: " + scrap, 100, 580);
        game.font.draw(game.batch, "Dalga: " + wave, 200, 580);

        game.font.draw(game.batch, "Kuleler: [1] Civi (50) | [2] Anahtar (75) | [3] Yag (70)", 10, 20);
        game.font.draw(game.batch, "[SPACE] Yeni Dalga", 10, 40);

        if (selectedTowerType != null) {
            game.font.draw(game.batch, "Secili: " + selectedTowerType, 400, 580);
        }

        if (isGameOver) {
            game.font.getData().setScale(2);
            if (garageHp > 0) {
                game.font.setColor(Color.GREEN);
                game.font.draw(game.batch, "KAZANDIN!", 300, 300);
            } else {
                game.font.setColor(Color.RED);
                game.font.draw(game.batch, "KAYBETTIN!", 300, 300);
            }
            game.font.getData().setScale(1);
        }

        game.batch.end();
    }

    private void handleInput() {
        if (isGameOver) return;

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) startNextWave();

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) selectedTowerType = "Civi";
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) selectedTowerType = "Anahtar";
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) selectedTowerType = "Yag";

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if (selectedTowerType != null) {
                float x = Gdx.input.getX();
                float y = Gdx.graphics.getHeight() - Gdx.input.getY();
                placeTower(x, y);
            }
        }
    }

    private void placeTower(float x, float y) {
        kule t = null;

        if ("Civi".equals(selectedTowerType) && scrap >= 50)
            t = new civiKulesi(x - 15, y - 15);

        if ("Anahtar".equals(selectedTowerType) && scrap >= 75)
            t = new anahtarKulesi(x - 15, y - 15);

        if ("Yag".equals(selectedTowerType) && scrap >= 70)
            t = new yagKulesi(x - 15, y - 15);

        if (t != null) {
            towers.add(t);
            scrap -= t.getCost();
            selectedTowerType = null;
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

        if (waveNum == 1) {
            spawnQueue.add(new crossMotor());
            spawnQueue.add(new crossMotor());
            spawnQueue.add(new zirhliKamyon());
            spawnQueue.add(new ucak());
        } else {
            Random r = new Random();
            int count = 5 + r.nextInt(6);
            for (int i = 0; i < count; i++) {
                int t = r.nextInt(3);
                if (t == 0) spawnQueue.add(new crossMotor());
                if (t == 1) spawnQueue.add(new zirhliKamyon());
                if (t == 2) spawnQueue.add(new ucak());
            }
        }
    }

    private void update(float delta) {
        if (isGameOver) return;

        // ➤ Spawn sistemi
        if (isWaveActive && spawnQueue.size > 0) {
            if (System.currentTimeMillis() - lastSpawnTime > SPAWN_INTERVAL) {
                dusman e = spawnQueue.removeIndex(0);
                e.setPath(path);
                enemies.add(e);
                lastSpawnTime = System.currentTimeMillis();
            }
        }

        // ➤ Düşman hareketi
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

        // ➤ Kule saldırısı
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

        // ➤ Dalga bitişi
        if (isWaveActive && spawnQueue.size == 0 && enemies.size == 0) {
            isWaveActive = false;
            if (wave >= 2) isGameOver = true;
        }
    }

    @Override
    public void show() {}
    @Override
    public void resize(int width, int height) {}
    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
