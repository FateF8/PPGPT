import codedraw.CodeDraw;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class FoodSource implements SpecialPoint{
    //„STYLE: objektorientierte Programmierung, Jede FoodSource (Objekt) ist verschieden und hat eigene Methoden, um auf die Daten der Objekte zuzugreifen.“
    private CodeDraw cd;
    private int scale = 3;

    private int x;
    private int y;
    private double scent;
    private int foodAmount;

    /**
     * Creates a FoodSource with the specified position and CodeDraw instance.
     *
     * @param x  The x-coordinate of the food source.
     * @param y  The y-coordinate of the food source.
     * @param cd The CodeDraw instance to use for drawing.
     */
    // precondition: x >= 0
    // precondition: y >= 0
    // precondition: cd != null
    // postcondition: foodAmount >= 20 && foodAmount <= 50
    public FoodSource(int x, int y, CodeDraw cd) {

        this.x = x;
        this.y = y;
        this.cd = cd;
        this.foodAmount = setRandomFoodAmount();
        this.scent = 0;
    }

    /**
     * Draws the food source on the specified CodeDraw instance.
     *
     * @param cd    The CodeDraw instance to use for drawing.
     * @param scale The scaling factor for drawing.
     */
    // precondition: cd != null
    // precondition: scale > 0
    // postcondition: food source should be drawn with the specified CodeDraw instance and scale
    public void draw(CodeDraw cd, int scale) {

        cd.setColor(new Color (8, 144, 0, 200));
        cd.fillCircle(x, y, foodAmount * 0.2);
    }

    @Override
    public void calcScentStrength(Scent scent) {
        this.scent = scent.getScent(x, y);
    }

    @Override
    public double getScentStrength() {
        return this.scent;
    }

    /**
     * Moves the food source by the specified increments in the x and y directions.
     *
     * @param dx The change in x-coordinate.
     * @param dy The change in y-coordinate.
     */
    // postcondition: food source's position should be updated by the specified increments
    public void move(int dx, int dy) {

        x += dx;
        y += dy;
    }

    /**
     * Decreases the food amount of the food source.
     */
    // postcondition: food amount should be decreased
    // postcondition: foodAmount >= 0
    public void decreaseFood() { foodAmount--; }

    // foodAmount = 20-50
    // postcondition: The random food amount should be within [20, 50].
    private int setRandomFoodAmount() { return this.foodAmount = (int) (Math.random() * 30) + 20; }

    public int getFoodAmount() { return foodAmount; }

    public int getX() { return x; }

    public int getY() {
        return y;
    }

    public Ellipse2D.Double getFoodSourceBoundaries() {
        return new Ellipse2D.Double(x, y, foodAmount * 0.2, foodAmount * 0.2);
    }
}
