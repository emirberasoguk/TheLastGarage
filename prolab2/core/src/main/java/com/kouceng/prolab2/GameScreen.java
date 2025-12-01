package com.kouceng.prolab2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import com.kouceng.prolab2.dusmanlar.crossMotor;
import com.kouceng.prolab2.dusmanlar.ucak;
import com.kouceng.prolab2.dusmanlar.zirhliKamyon;
import com.kouceng.prolab2.dusmanlar.dusman;

import com.kouceng.prolab2.kuleler.kule;
import com.kouceng.prolab2.kuleler.anahtarKulesi;
import com.kouceng.prolab2.kuleler.civiKulesi;
import com.kouceng.prolab2.kuleler.yagKulesi;

import java.util.Iterator;
import java.util.Random;

public class GameScreen implements Screen {

    private final Prolab2 game;
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
    private final int SPAWN_INTERVAL = 1500;

    // Input
    private String selectedTowerType = null; // "Civata", "Anahtar", "Yaglama"

    public GameScreen(Prolab2 game) {
        this.game = game;
        this.enemies = new Array<>();
        this.towers = new Array<>();
        this.path = new Array<>();
        this.spawnQueue = new Array<>();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);

        shapeRenderer = new ShapeRenderer();

        initPath();
        System.out.println("Simülasyon Başladı. Can: " + garageHp + ", Hurda: " + scrap);
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

        // Render
        Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);
        game.batch.setProjectionMatrix(camera.combined);

        // --- WORLD DRAW ---
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Draw path
        shapeRenderer.setColor(Color.DARK_GRAY);
        if (path.size > 0) {
            Vector2 p1 = path.get(0);
            for (int i = 1; i < path.size; i++) {
                Vector2 p2 = path.get(i);
                shapeRenderer.rectLine(p1, p2, 5);
                p1 = p2;
            }
        }

        // Garage
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(700, 280, 50, 40);

        // Towers
        for (kule t : towers) {
            t.draw(shapeRenderer);
        }

        // Enemies
        for (dusman e : enemies) {
            e.draw(shapeRenderer);
        }

        shapeRenderer.end();

        // --- UI ---
        game.batch.begin();

        game.font.setColor(Color.BLACK);
        game.font.draw(game.batch, "Can: " + garageHp, 10, 580);
        game.font.draw(game.batch, "Hurda: " + scrap, 100, 580);
        game.font.draw(game.batch, "Dalga: " + wave, 200, 580);

        game.font.draw(game.batch,
            "Kontroller: [SPACE] Dalga | [1] Civata | [2] Anahtar | [3] Yaglama | Sol Tik: Kule Koy",
            10, 20);

        if (selectedTowerType != null) {
            game.font.draw(game.batch, "Secili Kule: " + selectedTowerType, 400, 580);
        }

        if (isGameOver) {
            game.font.getData().setScale(2);
            if (garageHp > 0) {
                game.font.setColor(Color.GREEN);
                game.font.draw(game.batch, "KAZANDINIZ!", 300, 300);
            } else {
                game.font.setColor(Color.RED);
                game.font.draw(game.batch, "KAYBETTINIZ!", 300, 300);
            }
            game.font.getData().setScale(1);
        }

        game.batch.end();
    }

    private void handleInput() {
        if (isGameOver) return;

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) startNextWave();

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) selectedTowerType = "Civata";
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) selectedTowerType = "Anahtar";
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) selectedTowerType = "Yaglama";

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && selectedTowerType != null) {
            float x = Gdx.input.getX();
            float y = Gdx.graphics.getHeight() - Gdx.input.getY();
            placeTower(x, y);
        }
    }

    private void startNextWave() {
        if (isWaveActive || isGameOver) return;
        wave++;
        System.out.println("Dalga " + wave + " Başladı.");
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
                else if (t == 1) spawnQueue.add(new zirhliKamyon());
                else spawnQueue.add(new ucak());
            }
        }
    }

    private void placeTower(float x, float y) {
        kule t = null;

        if ("Civata".equals(selectedTowerType) && scrap >= 50)
            t = new anahtarKulesi(x - 15, y - 15);  // Civata = anahtarKulesi

        else if ("Anahtar".equals(selectedTowerType) && scrap >= 75)
            t = new civiKulesi(x - 15, y - 15);     // Anahtar = civiKulesi

        else if ("Yaglama".equals(selectedTowerType) && scrap >= 70)
            t = new yagKulesi(x - 15, y - 15);      // Yaglama = yagKulesi

        if (t != null) {
            towers.add(t);
            scrap -= t.getCost();
            System.out.println(selectedTowerType + " Kulesi inşa edildi.");
            selectedTowerType = null;
        }
    }

    private void update(float delta) {
        if (isGameOver) return;

        // Spawn enemies
        if (isWaveActive && spawnQueue.size > 0) {
            if (System.currentTimeMillis() - lastSpawnTime > SPAWN_INTERVAL) {
                dusman e = spawnQueue.removeIndex(0);
                e.setPath(path);
                enemies.add(e);
                lastSpawnTime = System.currentTimeMillis();
            }
        } else if (isWaveActive && spawnQueue.size == 0 && enemies.size == 0) {
            isWaveActive = false;
            if (wave >= 2) isGameOver = true;
        }

        // Move enemies
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

        // Towers attack
        for (kule t : towers) {
            if (t.canAttack()) {
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
        }
    }

    @Override
    public void resize(int w, int h) {}
    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void show() {}
    @Override
    public void hide() {}

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
