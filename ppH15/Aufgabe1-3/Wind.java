import codedraw.CodeDraw;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Wind {

    private int x, y;
    private int width, height;
    private int strength;
    private int direction;

    // invariant: strength should be in the range [1, 5], it represents the number of fields an object can be moved

    /**
     * Creates a Wind instance with the specified parameters.
     *
     * @param x         The x-coordinate of the wind's position.
     * @param y         The y-coordinate of the wind's position.
     * @param width     The width of the wind.
     * @param height    The height of the wind.
     * @param strength  The strength of the wind (1-5).
     * @param direction The direction of the wind (0, 45, 90, 135, 180, 225, 270, 315 degrees).
     */
    // precondition: strength >= 1 && strength <= 5, direction % 45 == 0
    public Wind(int x, int y, int width, int height, int strength, int direction) {

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.strength = strength;
        this.direction = direction;
    }

    private static final Map<Integer, int[]> move = new HashMap<>();
    private static int scale = 3;
    static {
        move.put(0, new int[]{scale, 0});
        move.put(45, new int[]{scale, -scale});
        move.put(90, new int[]{0, -scale});
        move.put(135, new int[]{-scale, -scale});
        move.put(180, new int[]{-scale, 0});
        move.put(225, new int[]{-scale, scale});
        move.put(270, new int[]{0, scale});
        move.put(315, new int[]{scale, scale});
    }

    // precondition: The move map should contain the current direction as a key.
    // postcondition: The position of the wind is updated based on the current direction and its strength.
    public void updatePosition() {
        x += move.get(this.direction)[0]  * this.strength;
        y += move.get(this.direction)[1] * this.strength;
    }

    // postcondition: returns true if the wind is outside the bounds of the simulation canvas, false otherwise.
    public boolean isOutsideSimulation(CodeDraw cd) {
        return x < 0 || y < 0 || x > cd.getWidth() || y > cd.getHeight();
    }

    // postcondition: the wind is drawn on the canvas
    public void draw(CodeDraw cd) {
        cd.setColor(new Color(135, 200, 250, 200));
        cd.fillRectangle(x, y, width, height);
    }

    // postcondition: returns a Rectangle object representing the boundaries of the wind.
    public Rectangle getBoundaries() {
        return new Rectangle(x, y, width, height);
    }

    public int getdx() { return move.get(this.direction)[0]; }

    public int getdy() {
        return move.get(this.direction)[1];
    }

    public int getStrength() { return strength; }
}
