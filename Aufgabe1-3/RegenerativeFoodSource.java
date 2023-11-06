import java.util.List;
import java.util.stream.Collectors;

public class RegenerativeFoodSource extends FoodSource {
    private final int x;
    private final int y;
    private int foodAmount;
    private final int maxFoodAmount;
    private final double regenerationRate;

    public RegenerativeFoodSource(int x, int y, int foodAmount, int maxFoodAmount, double regenerationRate) {
        super(x, y, foodAmount);
        this.x = x;
        this.y = y;
        this.foodAmount = foodAmount;
        this.maxFoodAmount = maxFoodAmount;
        this.regenerationRate = regenerationRate;
    }

    // Regenerate method that creates a new instance with increased food amount
    public void regenerate() {
        this.foodAmount = Math.min(maxFoodAmount, (int) (this.foodAmount * regenerationRate));
    }

    /*public RegenerativeFoodSource regenerate() {
        int newFoodAmount = Math.min(maxFoodAmount, (int) (foodAmount * regenerationRate));
        return new RegenerativeFoodSource(x, y, newFoodAmount, maxFoodAmount, regenerationRate);
    }
*/
 /*   public static List<RegenerativeFoodSource> applyRegeneration() {
        return rFoodSources .stream()                                   // "erzeugende Operation": konvertiert FoodSource Liste zu einem Stream
                            .map(RegenerativeFoodSource::regenerate)    // "modifizierende Operation": regenerate an jedes Element vom Stream angewendet
                            .collect(Collectors.toList());              // "abschließende Operation": jedes Element wird in neue Liste gesammelt
    }
*/
    public int getX() { return x; }
    public int getY() { return y; }
    public int getFoodAmount() { return foodAmount; }

}
