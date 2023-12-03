import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OtherInsectActions {

    // precondition: insect != null
    // precondition: specialPoints != null
    public static List<SpecialPoint> moveAndEat(OtherInsect insect, List<SpecialPoint> specialPoints) {

        // STYLE: "Funktionale Programmierung"
        // Dieser Abschnitt verwendet Java 8 Streams, um die FoodSource Objekte aus der Liste der specialPoints
        // herauszufiltern und in eine FoodSource Liste abzubilden. Durch die Streams werden Seiteneffekte vermieden
        // und die ursprüngliche Liste 'specialPoints' bleibt unverändert.
        List<FoodSource> foodSources = specialPoints.stream()
                .filter(point -> point instanceof FoodSource)
                .map(point -> (FoodSource) point)
                .collect(Collectors.toList());


        // STYLE: "Funktionale Programmierung"
        // Hier kommt ebenfalls Java 8 Stream zum Einsatz. Es wird die nächste FoodSource mithilfe der 'min'-Methode,
        // die rein funktional ist, gefunden. Sie verändert nicht die Quelle des Streams und erhält somit die
        // referenzielle Transparenz. Anschließend wird mit "ifPresent" untersucht, ob die FoodSource bereits erreicht
        // wurde.
        Optional<FoodSource> nearestFood = foodSources.stream()
                .min((fs1, fs2) -> compareDistance(insect, fs1, fs2));

        // Move toward the food source, when reached eat it
        nearestFood.ifPresent(foodSource -> {
            if (!reachedFood(foodSource, insect)) {
                if (!insect.isHasEaten()) {
                    moveToTarget(insect, foodSource.getX(), foodSource.getY());
                }
            } else {
                if (foodSource.getFoodAmount() > 0) {
                    foodSource.decreaseFood();
                } else {
                    insect.setHasEaten(true);
                }
            }
            if (insect.isHasEaten()) {
                moveToTarget(insect, insect.getSpawnX(), insect.getSpawnY());
            }
        });

        return specialPoints;
    }

    // precondition: insect != null
    // precondition: targetX != 0
    // precondition: targetY != 0
    // postcondition: insect.getX() == targetX || insect.getY() == targetY
    private static void moveToTarget(OtherInsect insect, int targetX, int targetY) {
            int moveX = insect.getX();
            int moveY = insect.getY();

            if (insect.getX() < targetX) {
                moveX += 3;
            } else if (insect.getX() > targetX) {
                moveX -= 3;
            }

            if (insect.getY() < targetY) {
                moveY += 3;
            } else if (insect.getY() > targetY) {
                moveY -= 3;
            }

            insect.setPosition(moveX, moveY);
    }

    // precondition: food != null
    // precondition: insect != null
    private static boolean reachedFood(FoodSource food, OtherInsect insect) {
        return insect.getX() == food.getX() && insect.getY() == food.getY();
    }

    // precondition: insect != null
    // precondition: fs1 != null
    // precondition: fs2 != null
    private static int compareDistance(OtherInsect insect, FoodSource fs1, FoodSource fs2) {
        return Double.compare(calcDistance(insect, fs1), calcDistance(insect, fs2));
    }

    // euklidische Norm: sqrt((x2 - x1)^2 + (y2 - y1)^2)
    // precondition: foodSource != null
    // precondition: insect != null
    private static double calcDistance(OtherInsect insect, FoodSource food) {
        int dx = food.getX() - insect.getX();
        int dy = food.getY() - insect.getY();
        return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
    }
}
