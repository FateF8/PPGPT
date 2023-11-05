import codedraw.CodeDraw;

import java.awt.*;

public class Anthill implements SpecialPoint{

    private int x, y;
    private int size;

    public Anthill(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
    }

    public void draw(CodeDraw cd, int size) {
        int canvasCenter = cd.getHeight() / 2;
        cd.setColor(Color.BLACK);
        cd.drawCircle(canvasCenter, canvasCenter, size);
        cd.setColor(new Color(92, 64, 51, 200));
        cd.fillCircle(canvasCenter, canvasCenter, size);
    }

    public boolean isAntInside(Ant ant) {
        int dx = ant.getX() - x;
        int dy = ant.getY() - y;
        return dx * dx + dy * dy <= size * size;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
