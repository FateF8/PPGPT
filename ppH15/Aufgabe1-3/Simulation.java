import codedraw.CodeDraw;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Simulation {
    private CodeDraw cd;
    // GOOD: Verwendung von Konstanten anstelle von magischen Zahlen für die Skalierung
    private int scale = 3;  // sollte ungerade sein
    private int canvasSize = 250 * scale; // 250x250 Felder, die scale x scale groß sind
    private int canvasCenter = canvasSize / 2;
    private List<SpecialPoint> specialPoints;
    private List<Ant> ants;
    private List<Ant> parallelAnts;
    //private Anthill anthill;
    //private List<FoodSource> foodSources;
    private int foodSpawnTimer = 500;
    //private List<Obstacle> obstacles;
    private List<Wind> wind;
    private Scent foodScent, anthillScent;
    private double decayFactor = 0.98;
    private int time = 1200;
    private int dayTimeBegin = 400;
    private int nightTimeBegin = 2200;
    private int anthillSize = scale * 5;
    private OtherInsect insect = new OtherInsect(-9, canvasCenter);

    // invariant: scale, canvasSize, anthillSize are all positive integers
    // invariant: specialPoints, ants, wind are non-null lists
    // invariant: decayFactor is a number between 0 and 1
    // invariant: time, dayTimeBegin, nightTimeBegin are integers representing time.
    // invariant: foodScent, anthillScent are instances of Scent.

    public Simulation() {
        cd = new CodeDraw(canvasSize, canvasSize);
        ants = new ArrayList<>();
        parallelAnts = new ArrayList<>();
        antsStart();
        foodScent = new Scent(decayFactor);
        anthillScent = new Scent(decayFactor);
        specialPoints = new ArrayList<>();
        specialPoints.add(new Anthill(canvasCenter, canvasCenter, anthillSize));
        //foodSources = new ArrayList<>();
        for (int i = 0; i < 5; i++) { specialPoints.add(spawnFoodSource()); }
        //obstacles = new ArrayList<>();
        for (int i = 0; i < 10; i++) { spawnObstacles(specialPoints);}
        wind = new ArrayList<>();
    }

    // initialize ants with random positions and directions
    private void antsStart() {
        int[] startOffset = {-9, -6, -3, 0, 3, 6, 9};
        for (int i = 0; i < 50; i++) {
            int x = canvasCenter + startOffset[(int) (Math.random() * startOffset.length)]; // random offset to spread out the ants
            int y = canvasCenter + startOffset[(int) (Math.random() * startOffset.length)];
            int direction = (int) (Math.random() * 8) * 45; // 0 = right, 90 = up, 180 = left, 270 = down
            ants.add(new Ant(x, y, direction));
            parallelAnts.add(new Ant(x, y, direction));
        }
    }

    // postcondition: Returns a new FoodSource within the canvasSize
    // postcondition: FoodSources are not at the edges
    private FoodSource spawnFoodSource() {
        int randomX = (int) ((Math.random() * (0.85 - 0.15) + 0.15 ) * canvasSize);
        int randomY = (int) ((Math.random() * (0.85 - 0.15) + 0.15 ) * canvasSize);

        // Pixel müssen in der Mitte vom Feld sein
        int foodX = randomX - (randomX + 1) % scale + 1;
        int foodY = randomY - (randomY + 1) % scale + 1;

        return new FoodSource(foodX, foodY, cd);
    }

    // GOOD: Methode erstellt zufällige Hindernisse im Spielfeld, was Lesbarkeit und Verständlichkeit des Codes fördert. Zweck der Methode ist klar und leicht nachvollziehbar, da sie eine klare Aufgabe hat.
    // BAD: redundanter Code, spawnFoodSource und spawnObstacles teilen sich viel Code
    // precondition: specialPoints is a list containing SpecialPoints.
    // postcondition: A new obstacle object is added to specialPoints without overlapping existing points.
    private void spawnObstacles(List<SpecialPoint> specialPoints) {
        int randomX = (int) ((Math.random() * (0.85 - 0.15) + 0.15 ) * canvasSize);
        int randomY = (int) ((Math.random() * (0.85 - 0.15) + 0.15 ) * canvasSize);

        // Pixel müssen in der Mitte vom Feld sein
        int obstacleX = randomX - (randomX + 1) % scale + 1;
        int obstacleY = randomY - (randomY + 1) % scale + 1;

        //TODO: sollte man eine eigene Methode schreiben?
        for (SpecialPoint s : specialPoints) {
            if (s instanceof FoodSource) {
                Rectangle obstacleBoundary = new Rectangle(obstacleX, obstacleY, scale, scale);
                if (((FoodSource) s).getFoodSourceBoundaries().intersects(obstacleBoundary)) {
                    spawnObstacles(specialPoints);
                    return;
                }
            } else if (s instanceof Anthill) {
                Ellipse2D anthillBoundary = ((Anthill) s).getAnthillBoundaries();
                Ellipse2D obstacleEllipse = new Ellipse2D.Double(obstacleX, obstacleY, scale, scale);
                if (anthillBoundary.intersects(obstacleEllipse.getBounds2D())) {
                    spawnObstacles(specialPoints);
                    return;
                }
            }
        }
        specialPoints.add(new Obstacle(obstacleX, obstacleY, cd));
    }


    // postcondition: result != null
    // postcondition: result.length == 2
    // postcondition: result[0] == 0 || result[0] == cd.getWidth()
    // postcondition: result[1] >= 0 && result[1] <= cd.getHeight()
    // postcondition: Returns an array with two integers that represents a random border coordinate.
    private int[] getRandomBorderCoord() {
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
        return new int[]{borderX, borderY};
    }

    // postcondition: A new wind object with random properties is added to the wind list.
    private void spawnWind() {
        int[] border = getRandomBorderCoord();
        int borderX = border[0];
        int borderY = border[1];

        int strength = (int) (Math.random() * 4) + 1;
        int windWidth = (int) (Math.random() * 10) + strength;
        int windHeight = (int) (Math.random() * 10) + strength;
        int direction = (int) (Math.random() * 8) * 45;

        wind.add(new Wind(borderX, borderY, windWidth, windHeight, strength, direction));
    }

    // precondition: wind != null
    // precondition: specialPoints != null
    // postcondition: If wind collides with a foodSource, foodSource should be moved and wind should disappear
    public void checkWindCollisions() {
        List<Wind> windToRemove = new ArrayList<>();

        for (Wind w : wind) {
            Rectangle windBoundary = w.getBoundaries();

            if (w.isOutsideSimulation(cd)) { windToRemove.add(w); }

            for (SpecialPoint s : specialPoints){
                if (s instanceof  FoodSource){
                    Rectangle foodBoundary = new Rectangle(s.getX(), s.getY(), (int) (((FoodSource) s).getFoodAmount() * 0.4), (int) (((FoodSource) s).getFoodAmount() * 0.4));
                    if (windBoundary.intersects(foodBoundary)) {
                        ((FoodSource) s).move(w.getdx() * w.getStrength(), w.getdy() * w.getStrength());
                        windToRemove.add(w);
                    }
                } else if (s instanceof Obstacle) {
                    Rectangle obstacleBoundary = new Rectangle(s.getX()-(int) ((Obstacle) s).getLevel()/2,s.getY()-(int) ((Obstacle) s).getLevel()/2,(int) ((Obstacle) s).getLevel(),(int) ((Obstacle) s).getLevel());
                    if (windBoundary.intersects(obstacleBoundary) && ((Obstacle) s).getLevel()/2 < w.getStrength()) {
                        ((Obstacle) s).move(w.getdx() * w.getStrength(), w.getdy() * w.getStrength());
                        windToRemove.add(w);
                    }
                }
            }

            /*for (FoodSource f : foodSources) {
                Rectangle foodBoundary = new Rectangle(f.getX(), f.getY(), (int) (f.getFoodAmount() * 0.4), (int) (f.getFoodAmount() * 0.4));
                if (windBoundary.intersects(foodBoundary)) {
                    f.move(w.getdx() * w.getStrength(), w.getdy() * w.getStrength());
                    windToRemove.add(w);
                }
            }

            for (Obstacle o : obstacles) {
                Rectangle obstacleBoundary = new Rectangle(o.getX()-(int)o.getLevel()/20,o.getY()-(int)o.getLevel()/20,(int)o.getLevel()/10,(int)o.getLevel()/10);
                if (windBoundary.intersects(obstacleBoundary) && o.getLevel()/20 < w.getStrength()) {
                    o.move(w.getdx() * w.getStrength(), w.getdy() * w.getStrength());
                    windToRemove.add(w);
                }
            }*/
        }
        wind.removeAll(windToRemove);
    }

    // precondition: All class fields are initialised and valid.
    // postcondition: The simulation is advancing by drawn the states step by step to the canvas.
    public void simulateAndDraw() {
        cd.clear();

        for (int x = 0; x < cd.getWidth(); x += scale) {
            for (int y = 0; y < cd.getHeight(); y += scale) {
                if (foodScent.getScent(x, y) > 0.2) {
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

        //specialPoints
        //„STYLE: objektorientierte Programmierung, Polymorphismus, specialPoints sind sehr abstrakt und daher nicht in ihrer eigentlichen Form erkennbar.“
        for (SpecialPoint s : specialPoints){
            // GOOD: dynamisches Binden
            s.draw(cd, anthillSize);
        }

        // anthill
        //anthill.draw(cd, scale);

        // foodSourceSpawn
        if (foodSpawnTimer <= 0) {
            specialPoints.add(spawnFoodSource());
            foodSpawnTimer = 500;
        }
        foodSpawnTimer--;

        /*for (FoodSource foodSource : foodSources) {
            foodSource.draw();
        }

        // obstacles
        for (Obstacle o : obstacles) {
            cd.setColor(new Color(255,153,143));
            cd.fillRectangle(o.getX()-o.getLevel()/20,o.getY()-o.getLevel()/20,o.getLevel()/10,o.getLevel()/10);
        }*/

        // Tag-Nacht-Zyklus

        if (time >= nightTimeBegin || time < dayTimeBegin) {
            for (Ant a : ants) {
                int distance = Math.abs(a.getX() - canvasCenter) + Math.abs(a.getY() - canvasCenter);
                if (distance > 500){
                    a.setStateNight();
                } else if (distance > 400 && time >= 2300 && time < dayTimeBegin){
                    a.setStateNight();
                } else if (distance > 300 && time >= 0 && time < dayTimeBegin) {
                    a.setStateNight();
                } else if (distance > 200 && time >= 100 && time < dayTimeBegin) {
                    a.setStateNight();
                } else if (distance > 100 && time >= 200 && time < dayTimeBegin) {
                    a.setStateNight();
                } else if (distance <= 100 && time >= 300 && time < dayTimeBegin) {
                    a.setStateNight();
                }
            }
        }

        if (time == dayTimeBegin) {
            for (Ant a : ants) {
                a.setStatePrevious();
            }
        }
        time++;
        time = time % 2400;

        if (time % 100 == 0){
            System.out.println("Uhrzeit: " + time / 100 + ":00");
        }

        // parallelAnts
        // STYLE: Parallele Programmierung
        // die parallele Programmierung erfolgt über .parallelStream(), wodurch sich die Ameisen
        // zur selben Zeit wie andere Ameisen bewegen können, da sie sich gegenseitig nicht stören und dasselbe mit draw.
        // Somit kann die Rechenzeit "gekürzt" werden.

        parallelAnts
                .parallelStream()
                .forEach(a -> a.antsMove(foodScent, anthillScent, specialPoints));

        cd.setColor(Color.GRAY);
        parallelAnts
                .parallelStream()
                .filter(a -> a.getAge() >= 12)
                .forEach(a -> cd.fillCircle(a.getX(), a.getY(), scale));

        cd.setColor(Color.BLACK);
        parallelAnts
                .parallelStream()
                .filter(a -> a.getAge() < 12)
                .forEach(a -> cd.fillCircle(a.getX(), a.getY(), scale));

        
        // ants
        for (Ant a : ants) {
            a.antsMove(foodScent, anthillScent, specialPoints);
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

        // other insect
        OtherInsectActions.moveAndEat(insect, specialPoints);
        cd.setColor(Color.DARK_GRAY);
        cd.fillCircle(insect.getX(), insect.getY(), scale * 3);

        // Rain
        Rain.rainTimer();
        if (Rain.isRaining()) {
            Rain.drawRain(scale, cd);
            Rain.weakenScent(anthillScent);
            Rain.weakenScent(foodScent);
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

