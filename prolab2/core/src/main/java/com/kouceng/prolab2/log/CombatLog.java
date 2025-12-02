package com.kouceng.prolab2.log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class CombatLog {

    private static final String FILE_NAME = "core\\src\\main\\java\\com\\kouceng\\prolab2\\log\\savas_gunlugu.txt";
    private static FileHandle file = Gdx.files.local(FILE_NAME);

    // ✔ Oyun başladığında çağrılır — eski log tamamen silinir
    public static void resetLog() {
        file.writeString("", false); // overwrite
        write("============================================");
        write("           S A V A S  G U N L U G U");
        write("============================================");
        write("");
        write("Yeni oyun başlatıldı.");
        write("--------------------------------------------");
    }

    // ✔ Tek satır ekleme
    public static void write(String text) {
        file.writeString(text + "\n", true);
    }

    // -----------------------------------------------------
    // DALGA LOG'LARI
    // -----------------------------------------------------
    public static void logWaveStart(int wave) {
        write("");
        write("=== DALGA " + wave + " BASLADI ===");
    }

    public static void logWaveEnd(int wave) {
        write("=== DALGA " + wave + " BITTI ===\n");
    }

    // -----------------------------------------------------
    // KULE LOG'LARI
    // -----------------------------------------------------

    /** Kule hedefe kilitlendiğinde */
    public static void logTowerAttack(String towerName, float x, float y, String enemyName) {
        write("[KULE] " + towerName + " (" + (int)x + "," + (int)y +
            ") → '" + enemyName + "' HEDEF ALINDI.");
    }

    /** Kule vurduğu anda hasar detaylarını yazmak isteyenler için geniş log */
    public static void logHitDetail(String towerName, String enemyName,
                                    int rawDamage, int armor, int finalDamage, int hpAfter) {

        write("[HIT] " + towerName + " → " + enemyName);
        write("     - Taban Hasar: " + rawDamage);
        write("     - Zırh: " + armor + " → Net Hasar: " + finalDamage);
        write("     - Yeni HP: " + hpAfter);
    }

    /** Basit hit logu (istersen üstteki yerine bunu kullanırsın) */
    public static void logHit(String towerName, String enemyName, int newHp) {
        write("[HIT] " + towerName + " → " + enemyName + " vuruldu. Yeni HP: " + newHp);
    }

    /** Alan etkili saldırılar için */
    public static void logAOE(String towerName, String centerEnemy, float radius) {
        write("[AOE] " + towerName + " alan saldırısı yaptı. Merkez: " +
            centerEnemy + " | Yarıçap: " + (int)radius);
    }

    /** Uçamayan hedef kısıtı */
    public static void logCannotHitFlying(String towerName, String enemyName) {
        write("[KURAL] " + towerName + " → " + enemyName +
            " hedef alınamadı (uçan hedef).");
    }

    /** Yavaşlatma olayı */
    public static void logSlowApplied(String enemyName, int durationMs) {
        write("[ETKI] " + enemyName + " yavaşlatıldı (" + (durationMs / 1000) + " sn).");
    }

    // -----------------------------------------------------
    // DÜŞMAN LOG'LARI
    // -----------------------------------------------------

    public static void logEnemySpawn(String enemyName, int hp, double armor, boolean flying) {
        write("[SPAWN] " + enemyName +
            " oyuna girdi → HP: " + hp + ", Zırh: " + armor +
            ", Uçan: " + (flying ? "Evet" : "Hayır"));
    }

    public static void logEnemyDeath(String enemyName, int reward) {
        write("[ÖLÜM] " + enemyName + " yok edildi. Ödül: +" + reward);
    }

    public static void logEnemyReachedBase(String enemyName, int damage) {
        write("[ACİL DURUM] " + enemyName +
            " garaja ulaştı! Verilen zarar: -" + damage + " HP");
    }
}
