//class that contains coordinates, scent, food and anthill information
public class SingleFieldInfo {

    private int x;
    private int y;
    private double foodStrength;
    private double anthillStrength;
    private boolean isFood;
    private boolean isAnthill;
    private boolean isObstacle;
    private Obstacle obstacle;

    private FoodSource foodSource;

    public SingleFieldInfo(int x, int y, double foodStrength, double anthillStrength, boolean isFood, boolean isAnthill, FoodSource foodSource, Obstacle obstacle, boolean isObstacle) {
        this.x = x;
        this.y = y;
        this.foodStrength = foodStrength;
        this.anthillStrength = anthillStrength;
        this.isFood = isFood;
        this.isAnthill = isAnthill;
        this.foodSource = foodSource;
        this.obstacle = obstacle;
        this.isObstacle = isObstacle;
    }

    public SingleFieldInfo(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public double getFoodStrength() { return foodStrength; }

    public double getAnthillStrength() { return anthillStrength; }

    public int getX() { return x; }

    public int getY() { return y; }

    public boolean isFood() { return isFood; }

    public FoodSource getFoodSource() {
        return foodSource;
    }

    public boolean isObstacle() { return isObstacle;}

    public Obstacle getObstacle() { return obstacle; }
}
