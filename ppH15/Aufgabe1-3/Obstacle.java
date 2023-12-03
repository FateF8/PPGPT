import codedraw.CodeDraw;

import java.awt.*;

public class Obstacle implements SpecialPoint{

    private int x, y;
    private double scent;
    private double level; // Obstacle-Level between [1;10]
    private CodeDraw cd;

    /**
     * Creates an Obstacle with the specified position and CodeDraw instance.
     *
     * @param x  The x-coordinate of the obstacle.
     * @param y  The y-coordinate of the obstacle.
     * @param cd The CodeDraw instance to use for drawing.
     */
    public Obstacle(int x, int y, CodeDraw cd) {
        this.x = x;
        this.y = y;
        this.cd = cd;
        this.level = Math.random() * 10 + 1;
        this.scent = 0;

        // Postcondition: The obstacle should be created with valid x, y, and a level within [1, 10].
        assert this.x == x : "Postcondition failed: x should be set correctly.";
        assert this.y == y : "Postcondition failed: y should be set correctly.";
        assert this.level >= 1 && this.level <= 10 : "Postcondition failed: level should be within [1, 10].";
    }

    /**
     * Draws the obstacle on the specified CodeDraw instance.
     *
     * @param cd    The CodeDraw instance to use for drawing.
     * @param scale The scaling factor for drawing.
     */
    public void draw(CodeDraw cd, int scale) {
        cd.setColor(new Color(255,153,143));
        cd.fillRectangle(x-level/2,y-level/2,level*2,level*2);

        // Postcondition: The obstacle should be drawn with the specified CodeDraw instance and scale.
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
     * Moves the obstacle by the specified increments in the x and y directions.
     *
     * @param dx The change in x-coordinate.
     * @param dy The change in y-coordinate.
     */
    public void move(int dx, int dy) {
        x += dx;
        y += dy;

        // Postcondition: The obstacle's position should be updated by the specified increments.
        assert x >= 0 : "Postcondition failed: x should remain non-negative after moving.";
        assert y >= 0 : "Postcondition failed: y should remain non-negative after moving.";
    }

    public int getX() { return x; }

    public int getY() { return y; }

    public double getLevel() { return level; }

    // precondition: level >= 1 && level <= 10
    // postcondition: The obstacle's level should be updated to the specified value.
    public void setLevel(double level) { this.level = level; }

    public Rectangle getObstacleBoundaries() { return new Rectangle((int) (x-level/2),(int) (y-level/2),(int) (level*2),(int) (level*2)); }
}
