import codedraw.CodeDraw;

import java.awt.*;

public class Obstacle implements SpecialPoint{

    private int x, y;
    private double level; // Obstacle-Level between [1;10]
    private CodeDraw cd;


    public Obstacle(int x, int y, CodeDraw cd) {
        this.x = x;
        this.y = y;
        this.cd = cd;
        this.level = Math.random() * 10 + 1;
    }

    public void draw(CodeDraw cd, int scale) {
        cd.setColor(new Color(255,153,143));
        cd.fillRectangle(getX()-getLevel()/2,getY()-getLevel()/2,getLevel(),getLevel());
    }

    public void move(int dx, int dy) {
        x += dx;
        y += dy;
    }

    public int getX() { return x; }

    public int getY() { return y; }

    public double getLevel() { return level; }

    public void setLevel(double level) { this.level = level; }
}
