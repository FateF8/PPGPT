import codedraw.CodeDraw;

import java.awt.*;

public class Rain {

    // „STYLE: prozedurales Paradigma, erkennbar durch die statische Variablen (= globale Variablen), statische Methoden (= Prozeduren)“
    private static boolean isRaining = false;
    private static double rainDecayFactor = 0.999;
    private static int rainSpawnTimer = (int) (Math.random() * 1000) + 250;
    private static int rainDespawnTimer = (int) (Math.random() * 1000) + 250;

    public static void startRain() {
        isRaining = true;
    }

    public static void stopRain() {
        isRaining = false;
    }

    public static void weakenScent(Scent scent) {
        if (isRaining) {
            scent.decayAllScents(rainDecayFactor);
        }
    }

    public static void rainTimer() {
        if (!isRaining) {
            rainSpawnTimer--;
            if (rainSpawnTimer <= 0) {
                startRain();
                rainSpawnTimer = (int) (Math.random() * 5000) + 2400;
            }
        } else {
            rainDespawnTimer--;
            if (rainDespawnTimer <= 0) {
                stopRain();
                rainDespawnTimer = (int) (Math.random() * 1000) + 250;
            }
        }
    }

    public static void drawRain(int scale, CodeDraw cd) {
        if (isRaining) {
            for (int i = 0; i < 10000; i++) {
                cd.setColor(new Color(77, 111, 222, 200));
                cd.fillCircle((int) (Math.random() * cd.getWidth() * scale), (int) (Math.random() * cd.getHeight() * scale), 1);
            }
        }
    }

    public static boolean isRaining() {
        return isRaining;
    }

    public static int setRainSpawnTimer(int timer) {
        return rainSpawnTimer = timer;
    }

    public static int setRainDespawnTimer(int timer) {
        return rainDespawnTimer = timer;
    }
}
