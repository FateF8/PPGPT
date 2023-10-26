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
    private Scent foodScent, anthillScent;
    private List<FoodSource> foodSources;
    private Anthill anthill;
    private int foodSpawnTimer = 500;

    public Simulation() {
        cd = new CodeDraw(canvasSize, canvasSize);
        ants = new ArrayList<>( );
        antsStart();
        foodScent = new Scent(0.99);
        anthillScent = new Scent(0.98);
        foodSources = new ArrayList<>();
        for (int i = 0; i < 5; i++) { spawnFoodSource(); }
        anthill = new Anthill(canvasCenter, canvasCenter);
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

    // draw the simulation
    public void draw() {
        cd.clear();

        for (int x = 0; x < cd.getWidth(); x += scale) {
            for (int y = 0; y < cd.getHeight(); y += scale) {
                if (foodScent.getScent(x, y) > anthillScent.getScent(x, y)) {
                    // scale the scent value to a green scale color
                    int ColorValue = 255 - Math.min((int) (foodScent.getScent(x, y) * 255), 255);
                    cd.setColor(new Color(ColorValue, 255,ColorValue));
                } else {
                    // scale the scent value to a brown scale color
                    int ColorValue = 255 - Math.min((int) (anthillScent.getScent(x, y) * 255), 255);
                    cd.setColor(new Color(Math.min((ColorValue + 64), 255), Math.min((ColorValue + 32), 255), ColorValue));
                }
                cd.fillRectangle(x, y, scale, scale);
            }
        }

        // ants
        for (Ant a : ants) {
            a.antsMove(foodScent, anthillScent, foodSources, anthill);
            if (a.getAge() >= 12){
                cd.setColor(Color.GRAY);
                cd.fillCircle(a.getX(), a.getY(), scale);
            } else {
                cd.setColor(Color.BLACK);
                cd.fillCircle(a.getX(), a.getY(), scale);
            }
        }

        // foodSource
        for (FoodSource f : foodSources) {
            f.drawFoodSource();
        }

        // anthill
        cd.setColor(new Color(92, 64, 51));
        cd.fillCircle(canvasCenter, canvasCenter, scale);


        foodScent.decayAllScents();
        anthillScent.decayAllScents();

        if (foodSpawnTimer <= 0) {
            spawnFoodSource();
            foodSpawnTimer = 500;
        }
        foodSpawnTimer--;

        cd.show(1);
    }
}

