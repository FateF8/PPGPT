import codedraw.CodeDraw;

import java.awt.*;

public class Obstacle {

    private int x, y;
    private double level; // Obstacle-Level between [0;100)


    public Obstacle(int x, int y) {
        this.x = x;
        this.y = y;
        this.level = 100;
    }

    public void move(int dx, int dy) {
        x += dx;
        y += dy;
    }

    public int getX() { return x; }

    public int getY() { return y; }

    public double getLevel() { return level; }

}
