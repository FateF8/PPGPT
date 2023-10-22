//class that contains coordinates, scent, food and anthill information
public class SingleFieldInfo {

    private int x;
    private int y;
    private double scentStrength;
    private boolean isFood;
    private boolean isAnthill;

    public SingleFieldInfo(int x, int y, double scentStrength, boolean isFood, boolean isAnthill) {
        this.x = x;
        this.y = y;
        this.scentStrength = scentStrength;
        this.isFood = isFood;
        this.isAnthill = isAnthill;
    }

    public double getScentStrength() { return scentStrength; }


    public int getX() { return x; }

    public int getY() { return y; }

    public boolean isFood() { return isFood; }
}
