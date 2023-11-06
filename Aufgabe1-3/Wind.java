import codedraw.CodeDraw;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Wind {

    private int x, y;
    private int width, height;
    private int strength; // 1-5, Anzahl an Felder die der Wind bewegen soll
    private int direction;


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

    public void updatePosition() {
        x += move.get(this.direction)[0]  * this.strength;
        y += move.get(this.direction)[1] * this.strength;
    }

    public boolean isOutsideSimulation(CodeDraw cd) {
        return x < 0 || y < 0 || x > cd.getWidth() || y > cd.getHeight();
    }

    public void draw(CodeDraw cd) {
        cd.setColor(new Color(135, 200, 250, 200));
        cd.fillRectangle(x, y, width, height);
    }

    public Rectangle getBoundaries() {
        return new Rectangle(x, y, width, height);
    }

    public int getdx() { return move.get(this.direction)[0]; }

    public int getdy() {
        return move.get(this.direction)[1];
    }

    public int getStrength() { return strength; }
}
