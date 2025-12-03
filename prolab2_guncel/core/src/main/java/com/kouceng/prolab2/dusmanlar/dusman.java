package com.kouceng.prolab2.dusmanlar;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public abstract class dusman {

    protected Texture texture;          // Sprite
    protected TextureRegion region;     // Rotation için zorunlu
    protected float width = 40;
    protected float height = 40;

    // HP bar için 1x1 white pixel (static, her düşman için 1 kere)
    private static Texture hpPixel;

    protected int hp;
    protected int maxHp;
    protected int damage;
    protected float speed;
    protected float baseSpeed;
    protected double armor;
    protected int reward;
    protected boolean isFlying;

    protected Vector2 position;
    protected Array<Vector2> path;
    protected int currentTargetIndex = 0;

    protected boolean reachedEnd = false;
    protected long slowEndTime = 0;

    protected float rotationDeg = 0f;

    private static int nextID = 100;   // tüm düşmanlara artan ID
    public String enemyID;             // bu düşmanın kendi ID’si


    public dusman(int hp, float speed, double armor, int reward, boolean isFlying, int damage) {
        this.hp = hp;
        this.maxHp = hp;
        this.damage = damage;
        this.speed = speed;
        this.baseSpeed = speed;
        this.armor = armor;
        this.reward = reward;
        this.isFlying = isFlying;

        this.position = new Vector2(0, 0);
        this.enemyID = "ID" + (nextID++);


        // HP Pixel sadece 1 kere yüklenir
        if (hpPixel == null) {
            Pixmap p = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            p.setColor(Color.WHITE);
            p.fill();
            hpPixel = new Texture(p);
            p.dispose();
        }
    }

    // Enemy texture'ı ayarlama fonksiyonu (BUNU alt sınıflar kullanacak)
    protected void setTexture(Texture tex) {
        this.texture = tex;
        this.region = new TextureRegion(tex);
    }

    // -------- PATH FOLLOW -------
    public void setPath(Array<Vector2> path) {
        this.path = path;

        if (path != null && path.size > 0) {
            this.position.set(path.get(0));
            this.currentTargetIndex = 1;
        }
    }

    public void move(float delta) {
        if (path == null || path.size == 0 || reachedEnd) return;

        float currentSpeed = isSlowed() ? baseSpeed / 2f : baseSpeed;

        if (currentTargetIndex >= path.size) {
            reachedEnd = true;
            return;
        }

        Vector2 target = path.get(currentTargetIndex);
        Vector2 dir = new Vector2(target).sub(position);

        float dist = dir.len();
        float moveAmount = currentSpeed * delta;

        // ROTATION HESABI
        if (dist > 0.001f) {
            rotationDeg = (float) Math.toDegrees(Math.atan2(dir.y, dir.x)) - 90f;
        }

        if (dist <= moveAmount) {
            position.set(target);
            currentTargetIndex++;
        } else {
            dir.nor();
            position.add(dir.scl(moveAmount));
        }
    }

    public void slowDown(long durationMs) {
        slowEndTime = System.currentTimeMillis() + durationMs;
    }

    public boolean isSlowed() {
        return System.currentTimeMillis() < slowEndTime;
    }

    // -------- RENDER -------
    public void render(SpriteBatch batch) {

        if (region == null) return;  // Texture yüklenmeden çizmeye çalışma

        // Slow tint
        if (isSlowed())
            batch.setColor(0.6f, 0.6f, 1f, 1f);
        else
            batch.setColor(Color.WHITE);

        batch.draw(
            region,
            position.x - width / 2,
            position.y - height / 2,
            width / 2, height / 2,  // origin
            width, height,
            1f, 1f,                // scale
            rotationDeg
        );

        batch.setColor(Color.WHITE);

        drawHpBar(batch);
    }

    private void drawHpBar(SpriteBatch batch) {
        float hpPercent = (float) hp / maxHp;

        float barWidth = width;
        float barHeight = 6;

        float bx = position.x - barWidth / 2;
        float by = position.y + height / 2 + 5;

        // Kırmızı zemin
        batch.setColor(Color.RED);
        batch.draw(hpPixel, bx, by, barWidth, barHeight);

        // Yeşil HP
        batch.setColor(Color.GREEN);
        batch.draw(hpPixel, bx, by, barWidth * hpPercent, barHeight);

        batch.setColor(Color.WHITE);
    }

    // -------- DAMAGE -------
    public void takeDamage(int dmg) {
        int net = (int)(dmg * (1.0 - armor / (armor + 100.0)));
        if (net < 1) net = 1;
        hp -= net;
    }

    public boolean isDead() { return hp <= 0; }
    public boolean hasReachedEnd() { return reachedEnd; }

    public float getX() { return position.x; }
    public float getY() { return position.y; }
    public int getReward() { return reward; }
    public int getDamage() { return damage; }
    public double getArmor() { return armor; }
    public boolean isFlyingEnemy() { return isFlying; }

    public void dispose() {
        if (texture != null) texture.dispose();
    }

    public String getID() {return enemyID; }
    public int getMaxHp() { return maxHp; }

    public int getHp() {
        return hp;
    }
}
