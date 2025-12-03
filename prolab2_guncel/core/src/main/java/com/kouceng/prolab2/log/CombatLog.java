package com.kouceng.prolab2.log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CombatLog {

    private static final String FILE_NAME = "savas_gunlugu.txt";
    private static FileHandle file = Gdx.files.local(FILE_NAME);

    private static String ts() {
        return "[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] ";
    }

    private static void log(String s) {
        String line = ts() + s;

        // DOSYAYA YAZ
        file.writeString(line + "\n", true);

        // TERMINALE YAZ
        System.out.println(line);
    }


    public static void resetLog() {
        file.writeString("", false);
        log("======================================");
        log("        SAVAŞ GÜNLÜĞÜ BAŞLADI");
        log("======================================");
    }

    // ================================
    // DALGA LOG
    // ================================
    public static void waveStart(int wave, String type, int count) {
        log("Dalga " + wave + " Başladı. (Tip: " + type + ", Sayı: " + count + ")");
    }

    public static void waveEnd(int wave) {
        log("Dalga " + wave + " Bitti.");
    }

    // ================================
    // SPAWN LOG
    // ================================
    public static void enemySpawn(String id, String type, int hp, int maxHp, double armor) {
        log("Düşman '" + type + "-" + id + "' (Can: " + hp + "/" + maxHp +
            ", Zırh:" + armor + ") haritaya girdi.");
    }

    // ================================
    // KULE HEDEF ALDI
    // ================================
    public static void towerTarget(String towerName, String id, String enemy) {
        log("Kule '" + towerName + "-" + id + "', '" + enemy + "'i hedefledi (öncelik: üss’e en yakın).");
    }

    // ================================
    // HASAR DETAYI
    // ================================
    public static void damageDetail(String shooter, int raw, double armor, int afterArmor, int net, int hpAfter, int maxHp) {
        log(shooter + " atışı: Taban " + raw +
            " → Zırhlı cezası %" + afterArmor +
            " → " + (raw - afterArmor) +
            "; Zırh formülü ile Net Hasar " + net + ".");
        log("Kalan Can: " + hpAfter + "/" + maxHp + ".");
    }

    // ================================
    // YAVAŞLATMA
    // ================================
    public static void slowApplied(String enemy, int percent, int seconds) {
        log("'" + enemy + "' yavaşlatıldı (%" + percent + ", " + seconds + " sn).");
    }

    // ================================
    // AOE
    // ================================
    public static void aoeHit(String tower, String center) {
        log("Top atışı (AOE): merkez " + center + ".");
    }

    public static void aoeDamage(String enemy, int net) {
        log("— '" + enemy + "' Net Hasar " + net + ".");
    }

    // ================================
    // ÖLÜM
    // ================================
    public static void death(String enemy, int reward, int totalScrap) {
        log("'" + enemy + "' öldü. Ödül +" + reward + ". Toplam Para: " + totalScrap + ".");
    }

    // ================================
    // ÜSSE ULAŞTI
    // ================================
    public static void reachedBase(String enemy, int baseHp, int dmg) {
        log("'" + enemy + "' üsse ulaştı. Oyuncu Canı: " + baseHp + " (-" + dmg + ")");
    }
}
