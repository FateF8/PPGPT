import codedraw.CodeDraw;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class Simulation {
    private CodeDraw cd;
    private int scale = 3;  // sollte ungerade sein
    private int canvasSize = 250 * scale; // 250x250 Felder, die scale x scale groß sind
    private int canvasCenter = canvasSize / 2;
    private List<Ant> ants;
    private Anthill anthill;
    private List<FoodSource> foodSources;
    private int foodSpawnTimer = 500;
    private List<Obstacle> obstacles;
    private List<Wind> wind;
    private Scent foodScent, anthillScent;
    private double decayFactor = 0.98;
    private int time = 1200;

    public Simulation() {
        cd = new CodeDraw(canvasSize, canvasSize);
        ants = new ArrayList<>();
        antsStart();
        foodScent = new Scent(decayFactor);
        anthillScent = new Scent(decayFactor);
        foodSources = new ArrayList<>();
        for (int i = 0; i < 5; i++) { spawnFoodSource(); }
        anthill = new Anthill(canvasCenter, canvasCenter);
        obstacles = new ArrayList<>();
        for (int i = 0; i < 10; i++) { spawnObstacles();}
        wind = new ArrayList<>();
    }

    // initialize ants with random positions and directions
    private void antsStart() {
        for (int i = 0; i < 100; i++) {
            int[] startOffset = {-9, -6, -3, 0, 3, 6, 9};
            int x = canvasCenter + startOffset[(int) (Math.random() * startOffset.length)]; // random offset to spread out the ants
            int y = canvasCenter + startOffset[(int) (Math.random() * startOffset.length)];
            int direction = (int) (Math.random() * 8) * 45; // 0 = right, 90 = up, 180 = left, 270 = down
            ants.add(new Ant(x, y, direction));
        }
    }

    private void spawnFoodSource() {
        int randomX = (int) ((Math.random() * (0.85 - 0.15) + 0.15 ) * canvasSize);
        int randomY = (int) ((Math.random() * (0.85 - 0.15) + 0.15 ) * canvasSize);

        // Pixel müssen in der Mitte vom Feld sein
        int foodX = randomX - (randomX + 1) % scale + 1;
        int foodY = randomY - (randomY + 1) % scale + 1;

        foodSources.add(new FoodSource(foodX, foodY, cd));
    }

    private void spawnObstacles() {
        int randomX = (int) ((Math.random() * (0.85 - 0.15) + 0.15 ) * canvasSize);
        int randomY = (int) ((Math.random() * (0.85 - 0.15) + 0.15 ) * canvasSize);

        // Pixel müssen in der Mitte vom Feld sein
        int obstacleX = randomX - (randomX + 1) % scale + 1;
        int obstacleY = randomY - (randomY + 1) % scale + 1;

        obstacles.add(new Obstacle(obstacleX, obstacleY));
    }

    private void spawnWind() {
        int borderX, borderY;
        if (Math.random() < 0.5) {
            if (Math.random() < 0.5) {
                borderX = 0;
            } else {
                borderX = cd.getWidth();
            }
            borderY = (int) (Math.random() * cd.getHeight());
        } else {
            if (Math.random() < 0.5) {
                borderY = 0;
            } else {
                borderY = cd.getHeight();
            }
            borderX = (int) (Math.random() * cd.getWidth());
        }

        int strength = (int) (Math.random() * 4) + 1;
        int windWidth = (int) (Math.random() * 10) + strength;
        int windHeight = (int) (Math.random() * 10) + strength;
        int direction = (int) (Math.random() * 8) * 45;

        wind.add(new Wind(borderX, borderY, windWidth, windHeight, strength, direction));
    }

    // If wind collides with a foodSource, foodSource should be moved and wind should disappear
    private void checkWindCollisions() {
        List<Wind> windToRemove = new ArrayList<>();

        for (Wind w : wind) {
            Rectangle windBoundary = w.getBoundaries();

            if (w.isOutsideSimulation(cd)) {
                windToRemove.add(w);
            }

            for (FoodSource f : foodSources) {
                Rectangle foodBoundary = new Rectangle(f.getX(), f.getY(), (int) (f.getFoodAmount() * 0.4), (int) (f.getFoodAmount() * 0.4));
                if (windBoundary.intersects(foodBoundary)) {
                    f.move(w.getdx(), w.getdy());
                    windToRemove.add(w);
                }
            }

            for (Obstacle o : obstacles) {
                Rectangle obstacleBoundary = new Rectangle(o.getX()-(int)o.getLevel()/20,o.getY()-(int)o.getLevel()/20,(int)o.getLevel()/10,(int)o.getLevel()/10); // TODO: Obstacle Größe soll variieren
                if (windBoundary.intersects(obstacleBoundary) && o.getLevel()/20 < w.getStrength()) {
                    o.move(w.getdx(), w.getdy());
                    windToRemove.add(w);
                }
            }
        }
        wind.removeAll(windToRemove);
    }

    // draw the simulation
    public void draw() {
        cd.clear();


        for (int x = 0; x < cd.getWidth(); x += scale) {
            for (int y = 0; y < cd.getHeight(); y += scale) {
                if (foodScent.getScent(x, y) >= anthillScent.getScent(x, y) && foodScent.getScent(x, y) > 0.1) {
                    // scale the scent value to a green scale color
                    int ColorValue = 255 - Math.min((int) (foodScent.getScent(x, y) * 255), 255);
                    cd.setColor(new Color(ColorValue, 255, ColorValue));
                } else /*if (foodScent.getScent(x, y) < anthillScent.getScent(x, y) && anthillScent.getScent(x, y) > 0.1)*/ {
                    // scale the scent value to a brown scale color
                    int ColorValue = 255 - Math.min((int) (anthillScent.getScent(x, y) * 255), 255);
                    cd.setColor(new Color(Math.min((ColorValue + 64), 255), Math.min((ColorValue + 32), 255), ColorValue));
                } /*else{
                    int c = -Math.abs(2 * time * 255 / 2400 - 255);
                    cd.setColor(new Color(255 + c, 255 + c, -c, 60));
                }*/
                cd.fillRectangle(x, y, scale, scale);
            }
        }
        int c =  -Math.abs(2 * time * 255 / 2400 - 255);
        cd.setColor(new Color( 255 + c,255 + c, -c, 25));
        cd.fillCircle(canvasCenter, canvasCenter, canvasSize);




        // anthill
        anthill.draw(cd, scale);

        // foodSource
        if (foodSpawnTimer <= 0) {
            spawnFoodSource();
            foodSpawnTimer = 500;
        }
        foodSpawnTimer--;

        for (FoodSource foodSource : foodSources) {
            foodSource.draw();
        }

        // obstacles
        for (Obstacle o : obstacles) {
            cd.setColor(new Color(255,153,143));
            cd.fillRectangle(o.getX()-o.getLevel()/20,o.getY()-o.getLevel()/20,o.getLevel()/10,o.getLevel()/10);
        }

        if (time >= 2200 || time < 400) {
            for (Ant a : ants) {
                a.setStateNight();
            }
        }
        if (time == 400) {
            for (Ant a : ants) {
                a.setStatePrevious();
            }
        }
        time++;
        time = time % 2400;
        if (time % 100 == 0){
            System.out.println("Uhrzeit: " + time / 100 + ":00");
        }



        // ants
        for (Ant a : ants) {
            a.antsMove(foodScent, anthillScent, foodSources, anthill, obstacles);
            if (a.getAge() >= 12){
                cd.setColor(Color.GRAY);
                cd.fillCircle(a.getX(), a.getY(), scale);
            } else {
                cd.setColor(Color.BLACK);
                cd.fillCircle(a.getX(), a.getY(), scale);
            }
        }

        foodScent.decayAllScents(decayFactor);
        anthillScent.decayAllScents(decayFactor);

        // Rain
        Rain.rainTimer();
        if (Rain.isRaining()) {
            Rain.drawRain(scale, cd);
            Rain.weakenScent(anthillScent);
        }

       // Wind
        spawnWind();
        checkWindCollisions();
        for (Wind wind : wind) {
            wind.updatePosition();
            wind.draw(cd);
        }

        cd.show(1);
    }
}

