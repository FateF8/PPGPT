//class that contains coordinates, scent, food and anthill information
public class SingleFieldInfo {

    private int x;
    private int y;
    private double foodStrength;
    private double anthillStrength;
    private boolean isFood;
    private boolean isAnthill;

    private FoodSource foodSource;

    public SingleFieldInfo(int x, int y, double foodStrength, double anthillStrength, boolean isFood, boolean isAnthill, FoodSource foodSource) {
        this.x = x;
        this.y = y;
        this.foodStrength = foodStrength;
        this.anthillStrength = anthillStrength;
        this.isFood = isFood;
        this.isAnthill = isAnthill;
        this.foodSource = foodSource;
    }

    public double getFoodStrength() { return foodStrength; }

    public double getAnthillStrength() {
        return anthillStrength;
    }

    public int getX() { return x; }

    public int getY() { return y; }

    public boolean isFood() { return isFood; }

    public FoodSource getFoodSource() {
        return foodSource;
    }
}
