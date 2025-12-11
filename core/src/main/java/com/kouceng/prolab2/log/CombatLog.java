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
        file.writeString(line + "\n", true);
        System.out.println(line);
    }

    public static void resetLog() {
        file.writeString("", false);
        log("======================================");
        log("        SAVAŞ GÜNLÜĞÜ BAŞLADI");
        log("======================================");
    }


    //dalga baslma bitme
    public static void waveStart(int wave, String type, int count) {
        log("Dalga " + wave + " Başladı. (Tip: " + type + ", Sayı: " + count + ")");
    }

    public static void waveEnd(int wave) {
        log("Dalga " + wave + " Bitti.");
    }


    //dusman girisi
    public static void enemySpawn(String id, String type, int hp, int maxHp, double armor) {
        log("Düşman '" + type + "-" + id + "' (Can: " + hp + "/" + maxHp +
            ", Zırh: " + (int)armor + ") haritaya girdi.");
    }


    //kule hedeflemesi
    public static void towerTarget(String tower, String towerId, String enemyFull) {
        log("Kule '" + tower + "-" + towerId + "', '" + enemyFull +
            "' hedef aldı (öncelik: üss’e en yakın).");
    }


    //hasar detay
    public static void damageDetail(String shooter, int raw, double armor, int net, int hpAfter, int maxHp) {

        // Gerçek blok yüzdesi
        double blockPct = armor / (armor + 100.0);

        // Gerçek engellenen miktar (raw - net)
        int blocked = raw - net;
        if (blocked < 0) blocked = 0;

        log(shooter + " atışı: Taban " + raw +
            " → Zırhlı Engelleme %" + (int)(blockPct * 100) +
            " → Engellenen " + blocked +
            "; Net Hasar " + net + ".");

        log("Kalan Can: " + hpAfter + "/" + maxHp + ".");
    }




    //yavaslatma
    public static void slowApplied(String enemyFull, int percent, int seconds) {
        log("'" + enemyFull + "' yavaşlatıldı (%" + percent + ", " + seconds + " sn).");
    }


    //alan hasarı
    public static void aoeCenter(String tower, String centerEnemy) {
        log("Kule '" + tower + "' alan vuruşu yaptı: merkez hedef '" + centerEnemy + "'.");
    }

    public static void aoeDamage(String enemy, int net) {
        log("— '" + enemy + "' Net Hasar " + net + ".");
    }


    //ölüm
    public static void death(String enemy, int reward, int totalScrap) {
        log("'" + enemy + "' öldü. Ödül +" + reward + ". Toplam Para: " + totalScrap + ".");
    }


    //base e grmesi dusmanın
    public static void reachedBase(String enemy, int baseHp, int dmg) {
        log("'" + enemy + "' üsse ulaştı. Oyuncu Canı: " + baseHp + " (-" + dmg + ")");
    }

    public static void aoeHit(String s, String s1) {
    }
}
