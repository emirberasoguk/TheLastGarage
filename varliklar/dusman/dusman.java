package com.kouceng.td.varliklar.dusman;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class dusman {
    protected float x,y; //koordinat
    protected float scale = 0.2f;
    protected float width,height;
    protected float speed; //hiz
    protected int health; //can
    protected int damage;
    protected int reward;
    protected int armour;
    private float slowTimer = 0f;
    private float slowFactor = 1f;

    public int getHealth() {
        return health;
    }
    public void setHealth(int health) {
        this.health = health;
    }
    public boolean isAlive() {
        return health > 0;
    }
    public boolean isSlowed() {
        return slowFactor < 1f;
    }


    public void takeDamage(int dmg) {
        // Formül: Net_Hasar = dmg * (1 - (armour / (armour + 100.0)))
        double netDamage = dmg * (1 - ((double) armour / (armour + 100.0)));

        // Kesirli hasarı integer'a çevir (örnek: aşağı yuvarla)
        int actualDamage = (int) Math.max(1, Math.round(netDamage));

        // Health'ten çıkar
        this.health -= actualDamage;
    }

    public void applySlow(float factor, float duration) {
        slowFactor = factor;       // hız çarpanı (0.5 = %50 yavaş)
        slowTimer = duration;      // süreyi başlat
    }

    public void update(float delta) {
        // Mevcut update kodun burada

        // Yavaşlama süresi kontrolü
        if (slowTimer > 0) {
            slowTimer -= delta;
            if (slowTimer <= 0) {
                slowFactor = 1f; // yavaşlama süresi bitti → normal hız
            }
        }

        // Hızı güncelle
        float actualSpeed = speed * slowFactor;
        x += actualSpeed * delta; // örnek hareket
    }

    // oyun motoru çizme fonk. (kaldırılabilir)
    public abstract void render(SpriteBatch batch);
}
