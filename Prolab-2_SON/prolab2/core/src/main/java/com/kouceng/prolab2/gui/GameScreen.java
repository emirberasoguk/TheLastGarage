package com.kouceng.prolab2.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.kouceng.prolab2.Prolab2;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

// UI
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.Align;

// Dusmanalr
import com.kouceng.prolab2.dusmanlar.*;

// Kuleler
import com.kouceng.prolab2.kuleler.*;
import com.kouceng.prolab2.kuleler.Mermi;

// Log
import com.kouceng.prolab2.log.CombatLog;

import java.util.Iterator;

public class GameScreen implements Screen {

    //oyun alanları ( degiskenleri )
    final Prolab2 game;
    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;

    private Texture mapTexture;
    private Texture towerSlotTexture;

    private Stage uiStage;
    private ImageButton btnCivi, btnAnahtar, btnYag,btnWaveStart;


    private Texture texBtnCivi, texBtnAnahtar, texBtnYag;
    private Texture texBtnCiviDisabled, texBtnAnahtarDisabled, texBtnYagDisabled;
    private Texture texWaveStart;
    private Texture ghostCivi, ghostAnahtar, ghostYag;
    private Texture panelTexture;
    private Texture texWin;
    private Texture texLose;
    private Texture texMenuBtn;

    private int scrap = 200;
    private int garageHp = 100;
    private int wave = 0;

    private boolean isGameOver = false;
    private boolean isPaused = false;
    private boolean isWaveActive = false;
    private float gameSpeed = 1f;

    private Array<dusman> enemies;
    private Array<kule> towers;
    private Array<Mermi> projectiles;
    private Array<Vector2> path;
    private Array<Vector2> towerSpots;
    private Array<dusman> spawnQueue;

    private long lastSpawn = 0;
    private final int SPAWN_INTERVAL = 1200;

    private String selectedTower = null;
    private Vector2 ghostSnap = null;
    private float ghostRange = 120;


    //constructor
    public GameScreen(Prolab2 game) {
        this.game = game;

        enemies = new Array<>();
        towers = new Array<>();
        projectiles = new Array<>();
        path = new Array<>();
        towerSpots = new Array<>();
        spawnQueue = new Array<>();

        CombatLog.resetLog();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);

        shapeRenderer = new ShapeRenderer();

            // texture yuklemeleri
        mapTexture = new Texture("map1.jpg");
        towerSlotTexture = new Texture("slot.png");
        panelTexture = new Texture("panel.png");
        texWin = new Texture("kazandiniz.png");
        texLose = new Texture("kaybettiniz.png");
        texMenuBtn = new Texture("ana_menu.png");
        texWaveStart = new Texture("wave_baslat.png");

        ghostCivi = new Texture("civi.png");
        ghostAnahtar = new Texture("anahtar.png");
        ghostYag = new Texture("yag.png");

        texBtnCivi = new Texture("civi_aktif.png");
        texBtnAnahtar = new Texture("anahtar_aktif.png");
        texBtnYag = new Texture("yag_aktif.png");

        texBtnCiviDisabled = new Texture("civi_pasif.png");
        texBtnAnahtarDisabled = new Texture("anahtar_pasif.png");
        texBtnYagDisabled = new Texture("yag_pasif.png");

        uiStage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(uiStage);

        initButtons();
        initPath();
        initTowerSpots();
    }


    //oyundaki butonlar
    private void initButtons() {

        btnCivi = new ImageButton(new TextureRegionDrawable(new TextureRegion(texBtnCivi)));
        setupButton(btnCivi, "Civi", 70, 360, 15);

        btnAnahtar = new ImageButton(new TextureRegionDrawable(new TextureRegion(texBtnAnahtar)));
        setupButton(btnAnahtar, "Anahtar", 50, 540, 15);

        btnYag = new ImageButton(new TextureRegionDrawable(new TextureRegion(texBtnYag)));
        setupButton(btnYag, "Yag", 75, 720, 15);

        uiStage.addActor(btnCivi);
        uiStage.addActor(btnAnahtar);
        uiStage.addActor(btnYag);
        //wave butonu
        btnWaveStart = new ImageButton(new TextureRegionDrawable(new TextureRegion(texWaveStart)));
        btnWaveStart.setSize(260, 100);
        btnWaveStart.setPosition(1010, 28); // sağ alt köşe
        btnWaveStart.setOrigin(Align.center);
        btnWaveStart.setTransform(true);

        //mouse ile buyut
        btnWaveStart.addListener(new ClickListener() {

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1 && !isWaveActive) {
                    btnWaveStart.setScale(1.10f);
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if (pointer == -1) {
                    btnWaveStart.setScale(1f);
                }
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!isWaveActive && !isGameOver) {
                    startNextWave();
                }
            }
        });
        uiStage.addActor(btnWaveStart);
    }

    private void setupButton(ImageButton btn, String type, int cost, float x, float y) {
        btn.setSize(220, 130);
        btn.setPosition(x, y);
        btn.setOrigin(Align.center);
        btn.setTransform(true);

        btn.addListener(new ClickListener() {

            @Override
            public void enter(InputEvent event, float xx, float yy, int pointer, Actor fromActor) {
                if (pointer == -1 && scrap >= cost) {
                    btn.setScale(1.15f);
                }
            }

            @Override
            public void exit(InputEvent event, float xx, float yy, int pointer, Actor toActor) {
                if (pointer == -1) {
                    btn.setScale(1f);
                }
            }

            @Override
            public void clicked(InputEvent e, float xx, float yy) {
                if (scrap >= cost) {
                    selectedTower = type;

                    if (type.equals("Civi")) ghostRange = CiviAgAtar.range;
                    else if (type.equals("Anahtar")) ghostRange = AnahtarMakinesi.range;
                    else if (type.equals("Yag")) ghostRange = YagSizdirici.range;
                }
            }
        });
    }




    //YOL ve KULE YERLERİ
    private void initPath() {
        path.add(new Vector2(-20, 330));
        path.add(new Vector2(420, 330));
        path.add(new Vector2(420, 480));
        path.add(new Vector2(670, 480));
        path.add(new Vector2(670, 180));
        path.add(new Vector2(1130, 180));
        path.add(new Vector2(1130, 450));
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


    //render loopu ( her framede tekrar calısır ) ( main yerine )
    @Override
    public void render(float delta) {

        // girdiler
        handleInput();

        updateButtonState();

        // oyun update
        if (!isPaused && !isGameOver)
            update(delta * gameSpeed);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        // harita + kule konumları
        game.batch.begin();
        game.batch.draw(mapTexture, 0, 0, 1280, 720);

        for (Vector2 spot : towerSpots)
            game.batch.draw(towerSlotTexture, spot.x - 40, spot.y - 40, 80, 80);

        game.batch.end();

        // ui stage
        uiStage.act(delta);
        uiStage.draw();
        // dalga butonu
        btnWaveStart.setVisible(!isWaveActive && !isGameOver);

        // üst panel
        drawTopPanel();

        // oyun nesneleri
        drawObjects();

       //yazılar
        drawUI();

        // son ekran
        if (isGameOver) {
            drawEndScreen();
            return;
        }
    }


    //button durumları update
    private void updateButtonState() {
        updateOne(btnCivi, 70, texBtnCivi, texBtnCiviDisabled);
        updateOne(btnAnahtar, 50, texBtnAnahtar, texBtnAnahtarDisabled);
        updateOne(btnYag, 75, texBtnYag, texBtnYagDisabled);
    }


    private void updateOne(ImageButton btn, int cost, Texture active, Texture inactive) {
        if (scrap < cost) {
            btn.setColor(Color.GRAY);
            btn.getStyle().imageUp = new TextureRegionDrawable(new TextureRegion(inactive));
        } else {
            btn.setColor(Color.WHITE);
            btn.getStyle().imageUp = new TextureRegionDrawable(new TextureRegion(active));
        }
    }


    //girdiler
    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) restartGame();
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) isPaused = !isPaused;

        if (isPaused || isGameOver) return;

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) gameSpeed += 0.5f;
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) gameSpeed = Math.max(0.5f, gameSpeed - 0.5f);

        if (selectedTower != null && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT))
            placeTower();
    }


    //kule koyma
    private void placeTower() {

        if (ghostSnap == null) return;

        kule t = null;

        if (selectedTower.equals("Civi") && scrap >= 70)
            t = new CiviAgAtar(ghostSnap.x, ghostSnap.y);
        else if (selectedTower.equals("Anahtar") && scrap >= 50)
            t = new AnahtarMakinesi(ghostSnap.x, ghostSnap.y);
        else if (selectedTower.equals("Yag") && scrap >= 75)
            t = new YagSizdirici(ghostSnap.x, ghostSnap.y);

        if (t == null) return;

        scrap -= t.getCost();
        towers.add(t);

        CombatLog.waveStart(wave, t.getClass().getSimpleName(), 1);

        towerSpots.removeValue(ghostSnap, true);

        selectedTower = null;
        ghostSnap = null;
    }


    //update
    private void update(float delta) {

        if (garageHp <= 0) {
            isGameOver = true;
            return;
        }

        if (isWaveActive && spawnQueue.size > 0) {

            dusman last = enemies.size > 0 ? enemies.peek() : null;

            boolean canSpawn = true;

            // spawn delay
            if (last != null) {
                float dist = Vector2.dst(last.getX(), last.getY(), path.get(0).x, path.get(0).y);
                if (dist < 120) {
                    canSpawn = false;
                }
            }

            if (canSpawn && System.currentTimeMillis() - lastSpawn > SPAWN_INTERVAL) {

                dusman e = spawnQueue.removeIndex(0);
                e.setPath(path);
                enemies.add(e);

                CombatLog.enemySpawn(e.getID(), e.getClass().getSimpleName(),
                    e.getHp(), e.getMaxHp(), e.getArmor());

                lastSpawn = System.currentTimeMillis();
            }
        }


        updateEnemies(delta);
        updateTowers();
        updateProjectiles();

        if (isWaveActive && spawnQueue.size == 0 && enemies.size == 0) {
            CombatLog.waveEnd(wave);
            isWaveActive = false;
        }
        //kaybetme kosulu
        if (garageHp <= 0 && !isGameOver) {
            isGameOver = true;
            return;
        }

        //kazanma kosulu
        if (!isWaveActive && spawnQueue.size == 0 && enemies.size == 0 && wave == 2) {
            if (!isGameOver) {
                isGameOver = true;
            }
        }
    }



    //dusman updateleri
    private void updateEnemies(float delta) {

        Iterator<dusman> it = enemies.iterator();

        while (it.hasNext()) {
            dusman e = it.next();

            e.move(delta);

            if (e.hasReachedEnd()) {

                garageHp -= e.getDamage();

                CombatLog.reachedBase(
                    e.getClass().getSimpleName() + "-" + e.getID(),
                    garageHp,
                    e.getDamage()
                );

                it.remove();
            }
        }
    }


    //kule updateleri
    private void updateTowers() {

        final float BASE_X = 1110;
        final float BASE_Y = 450;

        for (kule t : towers) {

            if (!t.canAttack()) continue;

            dusman target = null;
            float best = Float.MAX_VALUE;

            for (dusman e : enemies) {

                if (!t.isInRange(e)) continue;

                float dist = Vector2.dst(e.getX(), e.getY(), BASE_X, BASE_Y);
                if (dist < best) {
                    best = dist;
                    target = e;
                }
            }

            if (target != null) {

                // yag dokucu ucan hedefi vuramama kontrolu
                if (t instanceof YagSizdirici && target.isFlyingEnemy())
                    continue;

                CombatLog.towerTarget(
                    t.getClass().getSimpleName(),
                    String.valueOf(t.getId()),
                    target.getClass().getSimpleName() + "-" + target.getID()
                );

                projectiles.add(t.attack(target));
            }

        }
    }



    //mermi updateleri
    private void updateProjectiles() {

        Iterator<Mermi> it = projectiles.iterator();

        while (it.hasNext()) {

            Mermi m = it.next();
            m.update(Gdx.graphics.getDeltaTime());

            if (!m.isActive()) {

                if (m.hasHit()) {
                    dusman e = m.getTarget();

                    int before = e.getHp();
                    m.getOwner().onHit(e, enemies);
                    int after = e.getHp();

                    int net = before - after;

                    CombatLog.damageDetail(
                        m.getOwner().getClass().getSimpleName() + "-" + m.getOwner().getId(),
                        m.getOwner().getDamage(),
                        e.getArmor(),
                        net,
                        e.getHp(),
                        e.getMaxHp()
                    );

                    if (e.isDead()) {
                        scrap += e.getReward();

                        CombatLog.death(
                            e.getClass().getSimpleName() + "-" + e.getID(),
                            e.getReward(),
                            scrap
                        );

                        enemies.removeValue(e, true);
                    }
                }

                it.remove();
            }
        }
    }



    //draw
    private void drawObjects() {

        game.batch.begin();
        for (kule t : towers) t.render(game.batch);
        for (dusman e : enemies) e.render(game.batch);
        for (Mermi m : projectiles) m.render(game.batch);
        game.batch.end();

        if (selectedTower != null)
            drawGhost();
    }

    private void drawTopPanel() {
        game.batch.begin();

        // Panel görüntüsü
        game.batch.draw(panelTexture, 20, 530, 420, 180);

        // Yazılar
        game.font.setColor(Color.GREEN);
        game.font.getData().setScale(1.1f);

        game.font.draw(game.batch, "" + garageHp, 120, 601);
        game.font.draw(game.batch, "" + scrap, 216, 599);
        game.font.draw(game.batch, "" + wave, 380, 601);

        game.font.getData().setScale(1f);

        game.batch.end();
    }

    private void drawGhost() {

        Vector3 m = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(m);

        ghostSnap = getClosestSpot(m.x, m.y);
        if (ghostSnap == null) return;

        float x = ghostSnap.x;
        float y = ghostSnap.y;

        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.25f);
        shapeRenderer.circle(x, y, ghostRange);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        Texture tex =
            selectedTower.equals("Civi") ? ghostCivi :
                selectedTower.equals("Anahtar") ? ghostAnahtar :
                    ghostYag;

        game.batch.begin();
        game.batch.setColor(0.6f, 0.6f, 0.6f, 0.5f);
        game.batch.draw(tex, x - 60, y - 55, 120, 110);
        game.batch.setColor(Color.WHITE);
        game.batch.end();
    }

    private void drawEndScreen() {

        //arka plan koyu gri
        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.75f);
        shapeRenderer.rect(0, 0, 1280, 720);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        game.batch.begin();

        //kazanma/kaybetme
        Texture resultTex = (garageHp > 0) ? texWin : texLose;

        float rw = resultTex.getWidth();
        float rh = resultTex.getHeight();
        float rx = (1280 - rw) / 2f;
        float ry = (720 - rh) / 2f + 80;

        game.batch.draw(resultTex, rx, ry);

        //anamenu buton
        float btnW = 300;
        float btnH = 110;

        float btnX = (1280 - btnW) / 2f;
        float btnY = ry - 100;

        //mouse hover
        Vector3 mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mouse);
        float mx = mouse.x;
        float my = mouse.y;

        boolean hover = (mx > btnX && mx < btnX + btnW &&
            my > btnY && my < btnY + btnH);

        float scale = hover ? 1.10f : 1f;

        float drawW = btnW * scale;
        float drawH = btnH * scale;

        float drawX = btnX - (drawW - btnW) / 2f;
        float drawY = btnY - (drawH - btnH) / 2f;

        game.batch.draw(texMenuBtn, drawX, drawY, drawW, drawH);

        game.batch.end();

        // anamenu donus
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if (hover) {
                game.setScreen(new MainMenuScreen(game));
                dispose();
            }
        }
    }




    private Vector2 getClosestSpot(float x, float y) {
        Vector2 best = null;
        float dmin = 40;

        for (Vector2 s : towerSpots) {
            float d = Vector2.dst(x, y, s.x, s.y);
            if (d < dmin) {
                dmin = d;
                best = s;
            }
        }

        return best;
    }


    //ui
    private void drawUI() {

        game.batch.begin();

        if (selectedTower != null)
            game.font.draw(game.batch, "Secili: " + selectedTower, 1000, 670);

        game.batch.end();
    }


    private void restartGame() {
        scrap = 200;
        garageHp = 100;
        wave = 0;
        isWaveActive = false;
        isGameOver = false;

        enemies.clear();
        towers.clear();
        projectiles.clear();
        spawnQueue.clear();

        initTowerSpots();

        CombatLog.resetLog();
    }


    private void startNextWave() {

        if (isWaveActive) return;

        wave++;
        spawnQueue.clear();

        //Dalga logu
        CombatLog.waveStart(wave, "Karışık", wave == 1 ? 4 : 8);

        if (wave == 1) {
            spawnQueue.add(new MotorluCapulcu());
            spawnQueue.add(new MotorluCapulcu());
            spawnQueue.add(new ZirhliKamyon());
            spawnQueue.add(new GozcuUcagi());
        }

        if (wave == 2) {
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

        isWaveActive = true;
    }


    @Override public void dispose() {
        mapTexture.dispose();
        towerSlotTexture.dispose();
        uiStage.dispose();
        shapeRenderer.dispose();
        panelTexture.dispose();


        ghostCivi.dispose();
        ghostAnahtar.dispose();
        ghostYag.dispose();
        texWin.dispose();
        texLose.dispose();
        texBtnCivi.dispose();
        texBtnAnahtar.dispose();
        texBtnYag.dispose();
        texMenuBtn.dispose();
    }

    @Override public void show() {}
    @Override public void resize(int w, int h) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
