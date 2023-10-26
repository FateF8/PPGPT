import codedraw.CodeDraw;

import java.awt.*;

public class FoodSource {
    private CodeDraw cd;
    private int scale = 3;

    private int x;
    private int y;
    private int foodAmount;

    public FoodSource(int x, int y, CodeDraw cd) {
        this.x = x;
        this.y = y;
        this.cd = cd;
        this.foodAmount = setRandomFoodAmount();
    }

    public void drawFoodSource() {
        cd.setColor(new Color (8, 144, 0));
        cd.fillCircle(x, y, foodAmount * 0.2);
    }

    public void decreaseFood() {
        foodAmount--;
    }


    // foodAmount = 20-50
    private int setRandomFoodAmount() {
        return this.foodAmount = (int) (Math.random() * 30) + 20;
    }

    public int getFoodAmount() { return foodAmount; }

    public int getX() { return x; }

    public int getY() {
        return y;
    }
}
