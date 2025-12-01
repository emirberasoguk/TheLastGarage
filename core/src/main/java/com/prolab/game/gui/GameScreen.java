package com.prolab.game.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.prolab.game.MyGdxGame;
import com.prolab.game.entity.*;
import java.util.Iterator;
import java.util.Random;

public class GameScreen implements Screen {

    final MyGdxGame game;
    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;

    // Game State
    private int scrap = 200;
    private int garageHp = 100;
    private int wave = 0;
    private boolean isGameOver = false;
    private boolean isWaveActive = false;

    // Entities
    private Array<Enemy> enemies;
    private Array<Tower> towers;
    private Array<Vector2> path;

    // Wave Management
    private Array<Enemy> spawnQueue;
    private long lastSpawnTime = 0;
    private final int SPAWN_INTERVAL = 1500;

    // Input
    private String selectedTowerType = null; // "Civata", "Anahtar", "Yaglama"

    public GameScreen(final MyGdxGame game) {
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
        // Flip Y coordinates for LibGDX (Origin Bottom-Left)
        // Original: (0, 300), (200, 300), (200, 100), (500, 100), (500, 400), (700, 400), (700, 300)
        // Height: 600
        path.add(new Vector2(0, 300));
        path.add(new Vector2(200, 300));
        path.add(new Vector2(200, 500)); // 600 - 100
        path.add(new Vector2(500, 500));
        path.add(new Vector2(500, 200)); // 600 - 400
        path.add(new Vector2(700, 200));
        path.add(new Vector2(700, 300));
    }

    @Override
    public void render(float delta) {
        // 1. Input Handling
        handleInput();

        // 2. Update Game Logic
        update(delta);

        // 3. Render
        Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1); // Light Gray
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);
        game.batch.setProjectionMatrix(camera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        
        // Draw Path
        shapeRenderer.setColor(Color.DARK_GRAY);
        if (path.size > 0) {
             // Draw lines between points. ShapeRenderer rectLine is good for thickness
            Vector2 p1 = path.get(0);
            for (int i = 1; i < path.size; i++) {
                Vector2 p2 = path.get(i);
                shapeRenderer.rectLine(p1, p2, 5); // 5px width
                p1 = p2;
            }
        }

        // Draw Garage
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(700, 280, 50, 40);

        // Draw Towers
        for (Tower t : towers) {
            t.draw(shapeRenderer);
        }

        // Draw Enemies
        for (Enemy e : enemies) {
            e.draw(shapeRenderer);
        }
        shapeRenderer.end();

        // Draw UI Text
        game.batch.begin();
        game.font.setColor(Color.BLACK);
        game.font.draw(game.batch, "Can: " + garageHp, 10, 580);
        game.font.draw(game.batch, "Hurda: " + scrap, 100, 580);
        game.font.draw(game.batch, "Dalga: " + wave, 200, 580);
        
        // Instructions
        game.font.draw(game.batch, "Kontroller: [SPACE] Sonraki Dalga | [1] Civata (50) | [2] Anahtar (75) | [3] Yaglama (70) | [Mouse Sol] Kule Koy", 10, 20);
        
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

        // Next Wave
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            startNextWave();
        }

        // Select Towers
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            selectedTowerType = "Civata";
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            selectedTowerType = "Anahtar";
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            selectedTowerType = "Yaglama";
        }

        // Place Tower
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
             if (selectedTowerType != null) {
                 // Invert Y for mouse input
                 float x = Gdx.input.getX();
                 float y = Gdx.graphics.getHeight() - Gdx.input.getY();
                 placeTower(x, y);
             }
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
            spawnQueue.add(new CrossMotor());
            spawnQueue.add(new CrossMotor());
            spawnQueue.add(new ZirhliKamyon());
            spawnQueue.add(new PervaneliUcak());
        } else {
            Random rand = new Random();
            int count = 5 + rand.nextInt(6); // 5-10
            spawnQueue.add(new CrossMotor());
            spawnQueue.add(new ZirhliKamyon());
            spawnQueue.add(new PervaneliUcak());
            
            for (int i = 3; i < count; i++) {
                int r = rand.nextInt(3);
                if (r == 0) spawnQueue.add(new CrossMotor());
                else if (r == 1) spawnQueue.add(new ZirhliKamyon());
                else spawnQueue.add(new PervaneliUcak());
            }
        }
    }

    private void placeTower(float x, float y) {
        Tower t = null;
        if ("Civata".equals(selectedTowerType)) {
            if (scrap >= 50) t = new CivataKulesi(x - 15, y - 15);
        } else if ("Anahtar".equals(selectedTowerType)) {
            if (scrap >= 75) t = new AnahtarFirlatici(x - 15, y - 15);
        } else if ("Yaglama".equals(selectedTowerType)) {
            if (scrap >= 70) t = new YaglamaKulesi(x - 15, y - 15);
        }
        
        if (t != null) {
            towers.add(t);
            scrap -= t.getCost();
            System.out.println(selectedTowerType + " Kulesi inşa edildi.");
            selectedTowerType = null; 
        }
    }

    private void update(float delta) {
        if (isGameOver) return;

        // 1. Spawn
        if (isWaveActive && spawnQueue.size > 0) {
            if (System.currentTimeMillis() - lastSpawnTime > SPAWN_INTERVAL) {
                Enemy e = spawnQueue.removeIndex(0);
                e.setPath(path);
                enemies.add(e);
                lastSpawnTime = System.currentTimeMillis();
            }
        } else if (isWaveActive && spawnQueue.size == 0 && enemies.size == 0) {
            isWaveActive = false;
            System.out.println("Dalga " + wave + " Bitti.");
            if (wave >= 2) {
                isGameOver = true;
            }
        }

        // 2. Enemies Move
        Iterator<Enemy> it = enemies.iterator();
        while (it.hasNext()) {
            Enemy e = it.next();
            e.move(delta);
            
            if (e.hasReachedEnd()) {
                int dmg = 0;
                if (e instanceof CrossMotor) dmg = 5;
                else if (e instanceof ZirhliKamyon) dmg = 10;
                else if (e instanceof PervaneliUcak) dmg = 5;
                
                garageHp -= dmg;
                it.remove();
                
                if (garageHp <= 0) {
                    isGameOver = true;
                    garageHp = 0;
                }
            }
        }

        // 3. Towers Attack
        for (Tower t : towers) {
            if (t.canAttack()) {
                Enemy target = null;
                for (Enemy e : enemies) {
                    if (t.isInRange(e)) {
                        if (t instanceof AnahtarFirlatici && e.isFlying()) continue;
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
        
        // Clean dead
        Iterator<Enemy> cleanIt = enemies.iterator();
        while (cleanIt.hasNext()) {
            Enemy e = cleanIt.next();
            if (e.isDead()) {
                scrap += e.getReward();
                cleanIt.remove();
            }
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
