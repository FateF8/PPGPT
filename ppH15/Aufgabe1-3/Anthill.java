import codedraw.CodeDraw;

import java.awt.*;

public class Anthill{

    private int x, y;

    public Anthill(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(CodeDraw cd, int scale) {
        int canvasCenter = cd.getHeight() / 2;
        cd.setColor(Color.BLACK);
        cd.drawCircle(canvasCenter, canvasCenter, scale * 5);
        cd.setColor(new Color(92, 64, 51, 200));
        cd.fillCircle(canvasCenter, canvasCenter, scale * 5);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
