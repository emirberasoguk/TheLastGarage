package com.kouceng.prolab2.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.kouceng.prolab2.Prolab2;

// Scene2D & UI
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.Align;

// Enemies
import com.kouceng.prolab2.dusmanlar.*;

// Towers
import com.kouceng.prolab2.kuleler.*;
import com.kouceng.prolab2.kuleler.Mermi;

// Combat Log
import com.kouceng.prolab2.log.CombatLog;

import java.util.Iterator;

public class GameScreen implements Screen {

    // Fade-in
    private float fadeInAlpha = 1f;
    private float fadeSpeed = 1.5f;

    final Prolab2 game;
    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;

    private Texture mapTexture;
    private Texture panel1;
    private Texture towerSlotTexture;

    // UI Stage & Buttons
    private Stage uiStage;
    private ImageButton btnCivi, btnAnahtar, btnYag;

    // New button textures
    private Texture texBtnCivi;
    private Texture texBtnAnahtar;
    private Texture texBtnYag;

    // NEW: GHOST textures
    private Texture texCiviGhost;
    private Texture texAnahtarGhost;
    private Texture texYagGhost;

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

    private Array<Vector2> towerSpots;

    private String selectedTowerType = null;
    private float towerGhostRange = 120;
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
        panel1 = new Texture("panel1.png");
        towerSlotTexture = new Texture("slot.png");

        // NEW - ghost PNG load
        texCiviGhost = new Texture("civi.png");
        texAnahtarGhost = new Texture("anahtar.png");
        texYagGhost = new Texture("yag.png");

        // Load new button textures
        texBtnCivi = new Texture("civi_buton_guncel.png");
        texBtnAnahtar = new Texture("anahtar_buton_guncel.png");
        texBtnYag = new Texture("yaglama_buton_guncel.png");

        // --- UI SETUP ---
        uiStage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(uiStage);

        // 1. Civi Button (Cost 70)
        TextureRegionDrawable drawableCivi = new TextureRegionDrawable(new TextureRegion(texBtnCivi));
        btnCivi = new ImageButton(drawableCivi);
        setupButton(btnCivi, "Civi", 70, 20, 15);

        // 2. Anahtar Button (Cost 50)
        TextureRegionDrawable drawableAnahtar = new TextureRegionDrawable(new TextureRegion(texBtnAnahtar));
        btnAnahtar = new ImageButton(drawableAnahtar);
        setupButton(btnAnahtar, "Anahtar", 50, 110, 15);

        // 3. Yag Button (Cost 75)
        TextureRegionDrawable drawableYag = new TextureRegionDrawable(new TextureRegion(texBtnYag));
        btnYag = new ImageButton(drawableYag);
        setupButton(btnYag, "Yag", 75, 200, 15);

        uiStage.addActor(btnCivi);
        uiStage.addActor(btnAnahtar);
        uiStage.addActor(btnYag);
        // ----------------

        initPath();
        initTowerSpots();
    }

    private void restartGame() {
        scrap = 200;
        garageHp = 100;
        wave = 0;
        isGameOver = false;
        isPaused = false;
        isWaveActive = false;

        enemies.clear();
        towers.clear();
        projectiles.clear();
        spawnQueue.clear();

        initTowerSpots();
        CombatLog.resetLog();
    }

    private void initPath() {
        path.add(new Vector2(-20, 330));
        path.add(new Vector2(400, 330));
        path.add(new Vector2(400, 480));
        path.add(new Vector2(655, 480));
        path.add(new Vector2(655, 180));
        path.add(new Vector2(1110, 180));
        path.add(new Vector2(1110, 450));

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
        towerSpots.clear();
        towerSpots.add(new Vector2(260, 230));
        towerSpots.add(new Vector2(320, 425));
        towerSpots.add(new Vector2(550, 390));
        towerSpots.add(new Vector2(1025, 280));
        towerSpots.add(new Vector2(760, 420));
        towerSpots.add(new Vector2(550, 580));
        towerSpots.add(new Vector2(780, 280));
    }

    private void setupButton(final ImageButton btn, final String type, final int cost, float x, float y) {
        btn.setSize(64, 64);
        btn.setPosition(x, y);
        btn.setTransform(true);
        btn.setOrigin(Align.center);

        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (scrap >= cost) {
                    selectedTowerType = type;
                    if (type.equals("Civi")) towerGhostRange = CiviAgAtar.range;
                    else if (type.equals("Anahtar")) towerGhostRange = AnahtarMakinesi.range;
                    else if (type.equals("Yag")) towerGhostRange = YagSizdirici.range;
                }
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1 && scrap >= cost) {
                    btn.setScale(1.2f);
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if (pointer == -1) {
                    btn.setScale(1.0f);
                }
            }
        });
    }

    @Override
    public void render(float delta) {

        handleInput();
        
        // UPDATE BUTTON STATES
        updateButtonState(btnCivi, 70);
        updateButtonState(btnAnahtar, 50);
        updateButtonState(btnYag, 75);

        if (!isGameOver && !isPaused)
            update(delta);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(mapTexture, 0, 0, 1280, 720);

        // SLOT RENDER (kule yerleşmemiş olanlar görünür)
        for (Vector2 spot : towerSpots)
            game.batch.draw(towerSlotTexture, spot.x - 40, spot.y - 40, 80, 80);

        game.batch.draw(panel1, 10, 10, 300, 60);
        game.batch.end();
        
        // Draw Buttons (Stage)
        uiStage.act(delta);
        uiStage.draw();

        drawObjects();
        drawUI();

        if (fadeInAlpha > 0f) {
            fadeInAlpha -= delta * fadeSpeed;
            if (fadeInAlpha < 0f) fadeInAlpha = 0f;

            Gdx.gl.glEnable(GL20.GL_BLEND);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0, 0, 0, fadeInAlpha);
            shapeRenderer.rect(0, 0, 1280, 720);
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }

        if (isGameOver) {
            if (garageHp <= 0) drawLose();
            else drawWin();
        }

        if (isPaused && !isGameOver) drawPause();
    }

    // ======================================================
    // OBJECTS DRAW
    // ======================================================
    private void drawObjects() {

        game.batch.begin();
        for (kule t : towers) {
            if (t instanceof CiviAgAtar)
                ((CiviAgAtar)t).render(game.batch);
            else if (t instanceof AnahtarMakinesi)
                ((AnahtarMakinesi)t).render(game.batch);
            else if (t instanceof YagSizdirici)
                ((YagSizdirici)t).render(game.batch);
        }
        game.batch.end();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (Mermi m : projectiles)
            m.render(shapeRenderer);

        for (dusman e : enemies)
            e.draw(shapeRenderer);

        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        if (selectedTowerType != null)
            drawGhost();
    }

    // ======================================================
// GHOST SYSTEM (GRİ + TRANSPARENT RANGE CIRCLE)
// ======================================================
    private void drawGhost() {

        // Mouse -> dünya koordinatına çevir
        Vector3 mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mouse);

        ghostSnapSpot = getClosestSpot(mouse.x, mouse.y);
        if (ghostSnapSpot == null) return;

        float x = ghostSnapSpot.x;
        float y = ghostSnapSpot.y;

        // === RANGE CIRCLE (ALFA İLE) ===
        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0f, 0f, 0f, 0.25f); // %25 opak siyah
        shapeRenderer.circle(x, y, towerGhostRange);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        // === GHOST TEXTURE (GRİ + TRANSPARENT) ===
        Texture tex = null;

        if ("Civi".equals(selectedTowerType))
            tex = texCiviGhost;
        else if ("Anahtar".equals(selectedTowerType))
            tex = texAnahtarGhost;
        else if ("Yag".equals(selectedTowerType))
            tex = texYagGhost;

        if (tex != null) {
            game.batch.begin();
            game.batch.setColor(0.6f, 0.6f, 0.6f, 0.55f); // gri + yarı şeffaf
            game.batch.draw(tex, x - 60, y - 55, 120, 110);
            game.batch.setColor(Color.WHITE);
            game.batch.end();
        }
    }


    private Vector2 getClosestSpot(float x, float y) {
        Vector2 best = null;
        float min = 40f;

        for (Vector2 s : towerSpots) {
            if (Vector2.dst(x, y, s.x, s.y) < min) {
                best = s;
                min = Vector2.dst(x, y, s.x, s.y);
            }
        }
        return best;
    }

    private void updateButtonState(ImageButton btn, int cost) {
        if (scrap < cost) {
            btn.setColor(Color.GRAY); // Paramız yetmiyorsa gri
            btn.setScale(1.0f); // Reset scale if it was hovered
        } else {
            // If mouse is NOT over it, reset color to white
            // We rely on listener to handle hover scale, but here we ensure color is correct
            if (btn.getColor().equals(Color.GRAY)) {
                btn.setColor(Color.WHITE);
            }
        }
    }

    // ======================================================
    // INPUT
    // ======================================================
    private void handleInput() {

        if (Gdx.input.isKeyJustPressed(Input.Keys.R))
            restartGame();

        if (Gdx.input.isKeyJustPressed(Input.Keys.P))
            isPaused = !isPaused;

        if (isPaused || isGameOver) return;

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) gameSpeed += 0.5f;
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) gameSpeed = Math.max(0.5f, gameSpeed - 0.5f);

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
            startNextWave();

        // KEYBOARD SELECTION REMOVED

        if (selectedTowerType != null &&
            Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) &&
            ghostSnapSpot != null)
            placeTower(ghostSnapSpot.x, ghostSnapSpot.y);
    }

    // ======================================================
    // PLACE TOWER (slot silme EKLENDİ)
    // ======================================================
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

        // REMOVE SLOT FROM MAP (YENİ)
        towerSpots.removeValue(ghostSnapSpot, true);

        CombatLog.write("[KULE] " + t.getClass().getSimpleName() +
            " yerlestirildi (" + (int)x + "," + (int)y + ")");

        ghostSnapSpot = null;
        selectedTowerType = null;
    }

    // ======================================================
    // UPDATE
    // ======================================================
    private void update(float delta) {

        delta *= gameSpeed;

        if (garageHp <= 0) {
            isGameOver = true;
            return;
        }

        if (wave == 2 && !isWaveActive && enemies.size == 0) {
            isGameOver = true;
            return;
        }

        if (isWaveActive && spawnQueue.size > 0) {
            if (System.currentTimeMillis() - lastSpawnTime > SPAWN_INTERVAL) {

                dusman e = spawnQueue.removeIndex(0);
                e.setPath(path);
                enemies.add(e);

                lastSpawnTime = System.currentTimeMillis();
            }
        }

        Iterator<dusman> it = enemies.iterator();
        while (it.hasNext()) {
            dusman e = it.next();
            e.move(delta);

            if (e.hasReachedEnd()) {
                garageHp -= e.getDamage();
                it.remove();
            }
        }

        final float BASE_X = 1110;
        final float BASE_Y = 450;

        for (kule t : towers) {
            if (!t.canAttack()) continue;

            dusman target = null;
            float bestDist = Float.MAX_VALUE;

            for (dusman e : enemies) {

                if (t instanceof YagSizdirici && e.isFlying()) continue;
                if (!t.isInRange(e)) continue;

                float d = Vector2.dst(e.getX(), e.getY(), BASE_X, BASE_Y);
                if (d < bestDist) {
                    bestDist = d;
                    target = e;
                }
            }

            if (target != null)
                projectiles.add(t.attack(target));
        }

        Iterator<Mermi> itP = projectiles.iterator();
        while (itP.hasNext()) {
            Mermi m = itP.next();
            m.update(delta);

            if (!m.isActive()) {

                if (m.hasHit()) {
                    dusman target = m.getTarget();
                    m.getOwner().onHit(target, enemies);

                    if (target.isDead()) {
                        scrap += target.getReward();
                        enemies.removeValue(target, true);
                    }
                }
                itP.remove();
            }
        }

        if (isWaveActive && spawnQueue.size == 0 && enemies.size == 0)
            isWaveActive = false;
    }

    private void startNextWave() {
        if (isWaveActive) return;

        wave++;
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
            spawnQueue.add(new GozcuUcagi());
            spawnQueue.add(new GozcuUcagi());
        }

        isWaveActive = true;
    }

    private void drawUI() {
        game.batch.begin();

        game.font.setColor(Color.BLACK);
        game.font.draw(game.batch, "Can: " + garageHp, 20, 700);
        game.font.draw(game.batch, "Hurda: " + scrap, 150, 700);
        game.font.draw(game.batch, "Dalga: " + wave, 300, 700);
        game.font.draw(game.batch, "Hiz: x" + gameSpeed, 1150, 700);

        if (selectedTowerType != null)
            game.font.draw(game.batch, "Secili: " + selectedTowerType, 1000, 670);

        game.batch.end();
    }

    private void drawLose() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.6f);
        shapeRenderer.rect(0, 0, 1280, 720);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        game.batch.begin();
        game.font.getData().setScale(3f);
        game.font.setColor(Color.RED);
        game.font.draw(game.batch, "KAYBETTINIZ!", 500, 400);
        game.font.getData().setScale(1f);
        game.batch.end();
    }

    private void drawWin() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 1, 0, 0.4f);
        shapeRenderer.rect(0, 0, 1280, 720);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        game.batch.begin();
        game.font.getData().setScale(3f);
        game.font.setColor(Color.GREEN);
        game.font.draw(game.batch, "KAZANDINIZ!", 500, 400);
        game.font.getData().setScale(1f);
        game.batch.end();
    }

    private void drawPause() {
        game.batch.begin();
        game.font.getData().setScale(3f);
        game.font.setColor(Color.YELLOW);
        game.batch.draw(panel1, 560, 400);
        game.font.getData().setScale(1f);
        game.batch.end();
    }

    @Override
    public void dispose() {
        if (uiStage != null) uiStage.dispose();
        shapeRenderer.dispose();
        towerSlotTexture.dispose();
        mapTexture.dispose();
        panel1.dispose();

        texCiviGhost.dispose();
        texAnahtarGhost.dispose();
        texYagGhost.dispose();

        CiviAgAtar.disposeTextures();
        AnahtarMakinesi.disposeTextures();
        YagSizdirici.disposeTextures();

        // Dispose new button textures
        if (texBtnCivi != null) texBtnCivi.dispose();
        if (texBtnAnahtar != null) texBtnAnahtar.dispose();
        if (texBtnYag != null) texBtnYag.dispose();
    }

    @Override public void show() {}
    @Override public void resize(int w, int h) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
