import codedraw.CodeDraw;

public class Point implements SpecialPoint{

    private int x;
    private int y;

    private double scent;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
        this.scent = 0;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public void draw(CodeDraw cd, int scale) {
    }

    @Override
    public void calcScentStrength(Scent scent) {
        this.scent = scent.getScent(x, y);
    }

    @Override
    public double getScentStrength() {
        return this.scent;
    }
}
