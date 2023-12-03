import codedraw.CodeDraw;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Anthill implements SpecialPoint{

    // Invariant: Anthill's location is within the bounds of the simulation
    private int x, y;
    private double scent;
    private int size;

    // precondition: x >= 0; y >= 0; size > 0
    public Anthill(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.scent = 0;
    }

    // precondition: cd != null; size > 0
    public void draw(CodeDraw cd, int size) {
        int canvasCenter = cd.getHeight() / 2;
        cd.setColor(Color.BLACK);
        cd.drawCircle(canvasCenter, canvasCenter, size);
        cd.setColor(new Color(92, 64, 51, 200));
        cd.fillCircle(canvasCenter, canvasCenter, size);
    }

    @Override
    public void calcScentStrength(Scent scent) {
        this.scent = scent.getScent(x, y);
    }

    @Override
    public double getScentStrength() {
        return this.scent;
    }

    // precondition: ant != null
    public boolean isAntInside(Ant ant) {
        int dx = ant.getX() - x;
        int dy = ant.getY() - y;
        return dx * dx + dy * dy <= size * size;
    }

    public int getX() { return x; }

    public int getY() { return y; }

    public Ellipse2D.Double getAnthillBoundaries() { return new Ellipse2D.Double(x, y, size, size); }
}
