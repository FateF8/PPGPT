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
    private Scent scent, foodscent;
    //private List<FoodSource> foodSources;

    private List<SpecialPoint> specialPoints;
    //private Anthill anthill;

    public Simulation() {
        cd = new CodeDraw(canvasSize, canvasSize);
        ants = new ArrayList<>( );
        antsStart();
        scent = new Scent(0.98);
        foodscent = new Scent(0.98);
        specialPoints = new ArrayList<>();
        //foodSources = new ArrayList<>();
        specialPoints.add(new Anthill(canvasCenter, canvasCenter));
        for (int i = 0; i < 5; i++) {
            int randomX = (int) (Math.random() * canvasSize);
            int randomY = (int) (Math.random() * canvasSize);

            // Pixel müssen in der Mitte vom Feld sein
            int foodX = randomX - (randomX + 1) % scale + 1;
            int foodY = randomY - (randomY + 1) % scale + 1;
            //foodSources.add(new FoodSource(foodX, foodY));
            specialPoints.add(new FoodSource(foodX, foodY));
        }
        //anthill = new Anthill(canvasCenter, canvasCenter);
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

    // sollte noch komplett entfernt werden
    public void antsMove() {
        for (Ant a : ants) {
            a.antsMove(scent, foodscent, specialPoints);
        }
    }

    // draw the simulation
    public void draw() {
        cd.clear();

        for (int x = 0; x < cd.getWidth(); x += scale) {
            for (int y = 0; y < cd.getHeight(); y += scale) {

                if (scent.getScent(x,y) > foodscent.getScent(x,y)) {
                    // scale the scent value to a bluescale color
                    int ColorValue = 255 - Math.min((int) (scent.getScent(x, y) * 255), 255);
                    cd.setColor(new Color(ColorValue,ColorValue,255));
                } else {
                    // scale the scent value to a redscale color
                    int ColorValue = 255 - Math.min((int) (foodscent.getScent(x, y) * 255), 255);
                    cd.setColor(new Color(255,ColorValue,ColorValue));
                }
                cd.fillRectangle(x, y, scale, scale);

            }
        }

        // ants
        for (Ant a : ants) {
            cd.setColor(Color.BLACK);
            cd.fillCircle(a.getX(), a.getY(), scale);
        }
/*
        // foodSource
        for (FoodSource f : foodSources) {
            cd.setColor(Color.GREEN);
            cd.fillCircle(f.getX(), f.getY(), 3 * scale); // zu Ellipse ändern und vllt Augen adden um Blickrichtung sehen zu können
        }

        // anthill
        cd.setColor(new Color(92, 64, 51));
        cd.fillCircle(canvasCenter, canvasCenter, scale);

 */

        for (SpecialPoint s: specialPoints) {
            if (s.getClass() == Anthill.class){
                cd.setColor(new Color(92, 64, 51));
                cd.fillCircle(canvasCenter, canvasCenter, scale);
            } else if (s.getClass() == FoodSource.class) {
                cd.setColor(Color.GREEN);
                cd.fillCircle(s.getX(), s.getY(), 3 * scale); // zu Ellipse ändern und vllt Augen adden um Blickrichtung sehen zu können
            }
        }

        scent.decayAllScents();
        foodscent.decayAllScents();

        cd.show(1);

    }
}

