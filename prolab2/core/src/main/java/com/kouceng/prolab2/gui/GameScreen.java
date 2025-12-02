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
import com.kouceng.prolab2.dusmanlar.*;

// Towers
import com.kouceng.prolab2.kuleler.*;
import com.kouceng.prolab2.kuleler.Mermi;

// Combat Log
import com.kouceng.prolab2.log.CombatLog;

import java.util.Iterator;

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
    private boolean isPaused = false;
    private boolean isWaveActive = false;
    private float gameSpeed = 1.0f;

    // Entities
    private Array<dusman> enemies;
    private Array<kule> towers;
    private Array<Mermi> projectiles;

    private Array<Vector2> path;
    private Array<Rectangle> pathRects;

    // Tower Spots
    private Array<Vector2> towerSpots;

    // Ghost Tower
    private String selectedTowerType = null;
    private float towerGhostRange = 100;
    private Vector2 ghostSnapSpot = null;

    // Waves
    private Array<dusman> spawnQueue;
    private long lastSpawnTime = 0;
    private final int SPAWN_INTERVAL = 1200;

    public GameScreen(final Prolab2 game) {
        this.game = game;

        CombatLog.resetLog();

        enemies = new Array<>();
        towers = new Array<>();
        projectiles = new Array<>();
        path = new Array<>();
        pathRects = new Array<>();
        spawnQueue = new Array<>();
        towerSpots = new Array<>();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);

        shapeRenderer = new ShapeRenderer();
        mapTexture = new Texture("MAP.png");

        initPath();
        initTowerSpots();
    }

    private void restartGame() {
        scrap = 200;
        garageHp = 100;
        wave = 0;
        isGameOver = false;
        isWaveActive = false;
        isPaused = false;

        enemies.clear();
        towers.clear();
        projectiles.clear();
        spawnQueue.clear();

        CombatLog.resetLog();
    }

    private void initPath() {
        path.add(new Vector2(-20, 330)); //start
        path.add(new Vector2(400, 330));
        path.add(new Vector2(400, 480));
        path.add(new Vector2(655, 480));
        path.add(new Vector2(655, 180));
        path.add(new Vector2(1110, 180));
        path.add(new Vector2(1110, 450)); //base

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

    private void initTowerSpots() {
        towerSpots.add(new Vector2(260, 260));
        towerSpots.add(new Vector2(320, 410));
        towerSpots.add(new Vector2(550, 390));
        towerSpots.add(new Vector2(1050, 260));
        towerSpots.add(new Vector2(750, 420));
        towerSpots.add(new Vector2(550, 560));
        towerSpots.add(new Vector2(750, 260));
    }

    @Override
    public void render(float delta) {

        if (!isGameOver && !isPaused) {
            handleInput();
            update(delta);
        } else {
            handleInput();  // Pause & Restart çalışsın
        }

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        // Background
        game.batch.begin();
        game.batch.draw(mapTexture, 0, 0, 1280, 720);
        game.batch.end();

        // Darken screen on lose
        if (isGameOver && garageHp <= 0) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0, 0, 0, 0.6f);
            shapeRenderer.rect(0, 0, 1280, 720);
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }

        // Green tint on win
        if (isGameOver && garageHp > 0) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0, 1, 0, 0.4f);
            shapeRenderer.rect(0, 0, 1280, 720);
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }

        // Draw objects
        drawGameObjects();

        drawUI();

        if (isGameOver) {
            if (garageHp <= 0) drawLoseText();
            else drawWinText();
        }

        if (isPaused && !isGameOver) drawPauseText();
    }

    private void drawPauseText() {
        game.batch.begin();
        game.font.getData().setScale(3f);
        game.font.setColor(Color.YELLOW);
        game.font.draw(game.batch, "PAUSE", 560, 400);
        game.font.getData().setScale(1f);
        game.batch.end();
    }

    private void drawLoseText() {
        game.batch.begin();
        game.font.getData().setScale(3f);
        game.font.setColor(Color.RED);
        game.font.draw(game.batch, "KAYBETTINIZ!", 510, 400);
        game.font.getData().setScale(1f);
        game.font.setColor(Color.WHITE);
        game.font.draw(game.batch, "Tekrar oynamak icin : R", 570, 360);
        game.batch.end();
    }

    private void drawWinText() {
        game.batch.begin();
        game.font.getData().setScale(3f);
        game.font.setColor(Color.GREEN);
        game.font.draw(game.batch, "KAZANDINIZ!", 510, 400);
        game.font.getData().setScale(1f);
        game.font.setColor(Color.BLACK);
        game.font.draw(game.batch, "Tekrar oynamak icin : R", 555, 360);
        game.batch.end();
    }

    private void drawGameObjects() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Tower slots
        if (!isGameOver) {
            shapeRenderer.setColor(0, 1, 0, 0.35f);
            for (Vector2 s : towerSpots)
                shapeRenderer.circle(s.x, s.y, 18);
        }

        // Towers
        for (kule t : towers)
            t.draw(shapeRenderer);

        // Bullets
        for (Mermi m : projectiles)
            m.render(shapeRenderer);

        // Enemies
        for (dusman e : enemies)
            e.draw(shapeRenderer);

        if (!isGameOver)
            drawGhostTower();

        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    // ------------------ Ghost Tower ---------------------
    private void drawGhostTower() {
        if (selectedTowerType == null) return;

        Vector3 m = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(m);

        ghostSnapSpot = getClosestSpot(m.x, m.y);
        if (ghostSnapSpot == null) return;

        shapeRenderer.setColor(0, 0, 0, 0.25f);
        shapeRenderer.circle(ghostSnapSpot.x, ghostSnapSpot.y, towerGhostRange);

        if (selectedTowerType.equals("Civi"))
            shapeRenderer.setColor(0.7f, 0.7f, 0.7f, 0.6f);
        else if (selectedTowerType.equals("Anahtar"))
            shapeRenderer.setColor(1f, 0.5f, 0f, 0.6f);
        else if (selectedTowerType.equals("Yag"))
            shapeRenderer.setColor(0f, 0f, 0f, 0.6f);

        shapeRenderer.rect(ghostSnapSpot.x - 15, ghostSnapSpot.y - 15, 30, 30);
    }

    private Vector2 getClosestSpot(float x, float y) {
        Vector2 best = null;
        float min = 40;
        for (Vector2 s : towerSpots) {
            float d = Vector2.dst(x, y, s.x, s.y);
            if (d < min) {
                best = s;
                min = d;
            }
        }
        return best;
    }

    // ------------------ INPUT ---------------------
    private void handleInput() {

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) restartGame();

        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) isPaused = !isPaused;

        if (isPaused || isGameOver) return;

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) gameSpeed += 0.5f;
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) gameSpeed = Math.max(0.5f, gameSpeed - 0.5f);

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
            startNextWave();

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            selectedTowerType = "Civi";
            towerGhostRange = CiviAgAtar.range;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            selectedTowerType = "Anahtar";
            towerGhostRange = AnahtarMakinesi.range;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            selectedTowerType = "Yag";
            towerGhostRange = YagSizdirici.range;
        }

        if (selectedTowerType != null &&
            Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) &&
            ghostSnapSpot != null) {

            placeTower(ghostSnapSpot.x, ghostSnapSpot.y);
        }
    }

    private void placeTower(float x, float y) {

        kule t = null;

        if (selectedTowerType.equals("Civi") && scrap >= 70)
            t = new CiviAgAtar(x, y);
        else if (selectedTowerType.equals("Anahtar") && scrap >= 50)
            t = new AnahtarMakinesi(x, y);
        else if (selectedTowerType.equals("Yag") && scrap >= 75)
            t = new YagSizdirici(x, y);

        if (t == null) return;

        scrap -= t.getCost();
        towers.add(t);

        CombatLog.write("[KULE] " + t.getClass().getSimpleName() +
            " yerlestirildi (" + (int)x + "," + (int)y + ")");

        towerSpots.removeValue(ghostSnapSpot, true);

        ghostSnapSpot = null;
        selectedTowerType = null;
    }

    // ------------------ UPDATE ---------------------
    private void update(float delta) {

        delta *= gameSpeed;

        if (garageHp <= 0 && !isGameOver) {
            isGameOver = true;
            return;
        }

        // WIN CONDITION → 2 wave bitti + base alive
        if (wave == 2 && !isWaveActive && enemies.size == 0 && !isGameOver) {
            isGameOver = true;
            return;
        }

        // Spawn enemies
        if (isWaveActive && spawnQueue.size > 0) {
            if (System.currentTimeMillis() - lastSpawnTime > SPAWN_INTERVAL) {

                dusman e = spawnQueue.removeIndex(0);
                e.setPath(path);

                CombatLog.write("[SPAWN] " + e.getClass().getSimpleName() +
                    " haritaya girdi. HP=" + e.getHp());

                enemies.add(e);
                lastSpawnTime = System.currentTimeMillis();
            }
        }

        // Enemy movement
        Iterator<dusman> it = enemies.iterator();
        while (it.hasNext()) {
            dusman e = it.next();
            e.move(delta);

            if (e.hasReachedEnd()) {
                garageHp -= e.getDamage();
                CombatLog.logEnemyReachedBase(e.getClass().getSimpleName(), e.getDamage());
                it.remove();
            }
        }

        // Tower attack AI – ENEMY CLOSEST TO BASE
        for (kule t : towers) {
            if (!t.canAttack()) continue;

            final float BASE_X = 1110;
            final float BASE_Y = 450;

            dusman target = null;
            float bestDistance = Float.MAX_VALUE;

            for (dusman e : enemies) {

                if (t instanceof YagSizdirici && e.isFlying())
                    continue;

                if (!t.isInRange(e))
                    continue;

                float distToBase = Vector2.dst(e.getX(), e.getY(), BASE_X, BASE_Y);

                if (distToBase < bestDistance) {
                    bestDistance = distToBase;
                    target = e;
                }
            }

            if (target != null) {
                CombatLog.logTowerAttack(
                    t.getClass().getSimpleName(),
                    t.getX(), t.getY(),
                    target.getClass().getSimpleName()
                );
                projectiles.add(t.attack(target));
            }
        }

        // Bullet movement
        Iterator<Mermi> itP = projectiles.iterator();
        while (itP.hasNext()) {
            Mermi m = itP.next();
            m.update(delta);

            if (!m.isActive()) {

                if (m.hasHit()) {
                    dusman target = m.getTarget();
                    m.getOwner().onHit(target, enemies);

                    CombatLog.logHit(
                        m.getOwner().getClass().getSimpleName(),
                        target.getClass().getSimpleName(),
                        target.isDead() ? 0 : target.getHp()
                    );

                    // Enemy death
                    Iterator<dusman> itE = enemies.iterator();
                    while (itE.hasNext()) {
                        dusman e = itE.next();
                        if (e.isDead()) {
                            scrap += e.getReward();
                            CombatLog.logEnemyDeath(
                                e.getClass().getSimpleName(),
                                e.getReward()
                            );
                            itE.remove();
                        }
                    }
                }

                itP.remove();
            }
        }

        // Wave end
        if (isWaveActive && spawnQueue.size == 0 && enemies.size == 0) {
            isWaveActive = false;
            CombatLog.logWaveEnd(wave);
        }
    }

    private void startNextWave() {
        if (isWaveActive)
            return;

        wave++;
        CombatLog.logWaveStart(wave);
        generateWave();
        isWaveActive = true;
    }

    private void generateWave() {

        spawnQueue.clear();

        if (wave == 1) {
            spawnQueue.add(new MotorluCapulcu());
            spawnQueue.add(new MotorluCapulcu());
            spawnQueue.add(new ZirhliKamyon());
            spawnQueue.add(new GozcuUcagi());
        }
        else if (wave == 2) {
            spawnQueue.add(new MotorluCapulcu());

            spawnQueue.add(new ZirhliKamyon());
            spawnQueue.add(new ZirhliKamyon());
            spawnQueue.add(new ZirhliKamyon());
            spawnQueue.add(new ZirhliKamyon());
            spawnQueue.add(new ZirhliKamyon());
            spawnQueue.add(new ZirhliKamyon());

            spawnQueue.add(new GozcuUcagi());
            spawnQueue.add(new GozcuUcagi());
        }
    }

    private void drawUI() {

        game.batch.begin();

        game.font.setColor(Color.BLACK);
        game.font.draw(game.batch, "Can: " + garageHp, 20, 700);
        game.font.draw(game.batch, "Hurda: " + scrap, 150, 700);
        game.font.draw(game.batch, "Dalga: " + wave, 300, 700);

        game.font.draw(game.batch,
            "[1] Civi | [2] Anahtar | [3] Yag | [SPACE] Wave | R: Restart | P: Pause",
            20, 40);

        game.font.draw(game.batch, "Hiz: x" + gameSpeed, 1150, 700);

        if (selectedTowerType != null)
            game.font.draw(game.batch, "Secili: " + selectedTowerType, 1000, 670);

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
