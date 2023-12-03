import java.util.*;

public class Ant {
    //„STYLE: objektorientierte Programmierung, Jede Ameise (Objekt) ist verschieden, z.B.: x,y, direction, state, age, usw..“

    private int scale = 3; // muss mit Simulation verknüpft werden
    private int canvasSize = 250 * scale;
    private int canvasCenter = canvasSize / 2;
    private int x, y;
    private int direction; // 0, 45, 90, 135, 180, 225, 270, 315
    private boolean hasFood;

    // GOOD: Verwendung von Enum, um Zustand der Ameise darzustellen (prozedual)
    enum State {
        EXPLORING,
        FORAGING,
        CARRYING_FOOD,
        RAIN,
        NIGHT
    }

    private State state;
    private State previousState;
    private Map<Integer, int[][]> neighborsDirectionOffSet = new HashMap<>();
    private Map<String, Integer> updateDirectionMap = new HashMap<>();

    // variables that may need to be adjusted
    private double strongScent = 0.2;
    private int weakScentSteps = 0;
    private int maxWeakSteps = 15;
    private double fuel;
    private double maxFuel = 3;
    private int age;
    private double strength; // Strength is between [1;7]
    private double decayFactor = 0.99;
    private LinkedList<Point> visitedFields = new LinkedList<>();


    /**
     * Creates an Ant with the specified initial position and direction.
     *
     * @param x         The initial x-coordinate of the ant.
     * @param y         The initial y-coordinate of the ant.
     * @param direction The initial direction of the ant.
     */
    public Ant(int x, int y, int direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.state = State.EXPLORING;
        initialiseNeighborsOffSet();
        initialiseUpdateDirection();
        this.age = (int) (Math.random() * 24);
        this.fuel = maxFuel;
        this.strength = Math.random() * 7 + 1;
    }

    // define offsets for neighboring cells in different directions
    private void initialiseNeighborsOffSet() {
        neighborsDirectionOffSet.put(0, new int[][]{{0, -scale}, {scale, -scale}, {scale, 0}, {scale, scale}, {0, scale}}); // go left, up-left, up, up-right, right
        neighborsDirectionOffSet.put(45, new int[][]{{-scale, -scale}, {0, -scale}, {scale, -scale}, {scale, 0}, {scale, scale}});
        neighborsDirectionOffSet.put(90, new int[][]{{-scale, 0}, {-scale, -scale}, {0, -scale}, {scale, -scale}, {scale, 0}});
        neighborsDirectionOffSet.put(135, new int[][]{{-scale, scale}, {-scale, 0}, {-scale, -scale}, {0, -scale}, {scale, -scale}});
        neighborsDirectionOffSet.put(180, new int[][]{{0, scale}, {-scale, scale}, {-scale, 0}, {-scale, -scale}, {0, -scale}});
        neighborsDirectionOffSet.put(225, new int[][]{{scale, scale}, {0, scale}, {-scale, scale}, {-scale, 0}, {-scale, -scale}});
        neighborsDirectionOffSet.put(270, new int[][]{{scale, 0}, {scale, scale}, {0, scale}, {-scale, scale}, {-scale, 0}});
        neighborsDirectionOffSet.put(315, new int[][]{{scale, -scale}, {scale, 0}, {scale, scale}, {0, scale}, {-scale, scale}});
    }

    private void initialiseUpdateDirection() {
        updateDirectionMap.put(scale + ",0", 0);            // Right
        updateDirectionMap.put(scale + "," + -scale, 45);   // Up-Right
        updateDirectionMap.put("0," + -scale, 90);          // Up
        updateDirectionMap.put(-scale + "," + -scale, 135); // Up-Left
        updateDirectionMap.put(-scale + ",0", 180);         // Left
        updateDirectionMap.put(-scale + "," + scale, 225);  // Down-Left
        updateDirectionMap.put("0," + scale, 270);          // Down
        updateDirectionMap.put(scale + "," + scale, 315);   // Down-Right
        updateDirectionMap.put("0,0", direction);
    }

    /**
     * Move the ant and update its state.
     *
     * @param foodScent      The scent information for food.
     * @param anthillScent   The scent information for the anthill.
     * @param specialPoints  List of special points (e.g., food sources, obstacles, anthill).
     */
    // GOOD: Klassenzusammenhalt ist hier hoch, da Methode mit der Ant-Klasse zusammenhängt und auf interne Ant-Daten zugreift.
    // BAD: In einer Methode wird Entscheidung getroffen, wie die Ameise sich bewegt, abhängig von ihrem Zustand. Hier wird auf
    //      dynamisches Binden verzichtet, was den Code schwerer verständlich machen kann. Man könnte es in mehrere Methoden aufteilen.
    // precondition: The input parameters should not be null.
    // postcondition: The state of the ant should be updated.
    // postcondition: The position of the ant should be updated.
    // postcondition: The age, fuel, and other attributes should be updated according to logic.
    public void antsMove(Scent foodScent, Scent anthillScent, List<SpecialPoint> specialPoints) {

        // move ants and update their states
        if (state == State.EXPLORING) {
            explore(foodScent, anthillScent, specialPoints);
            anthillScent.increaseScent(x, y, fuel);
            visitedFields.addFirst(new Point(x,y));
            decayFuel();
        } else if (state == State.FORAGING) {
            forage(foodScent, anthillScent, specialPoints);
            anthillScent.increaseScent(x, y, fuel);
            visitedFields.addFirst(new Point(x,y));
            decayFuel();
        } else if (state == State.CARRYING_FOOD) {
            carry(foodScent, anthillScent, specialPoints);
            foodScent.increaseScent(x, y, 1);
        } else if (state == State.RAIN){
            carry(foodScent, anthillScent, specialPoints);
        } else {
            carry(foodScent, anthillScent, specialPoints);
            if (previousState == State.CARRYING_FOOD){
                foodScent.increaseScent(x, y, 1);
            } else {
                anthillScent.increaseScent(x, y, fuel);
                decayFuel();
            }
        }
    }

    // GOOD: weitgehender Verzicht auf Verwendung von globalen Variablen, Nachbarn werden mittels übergebene Parameter ermittelt (prozedual)
    // GOOD: Methode verwendet übergebenen Parameter, um unmittelbare Nachbarnfelder zu ermitteln, wodurch die Objektkopplung gering bleibt.
    // get neighbors for the ant to explore
    // precondition: The input parameters should not be null.
    // postcondition: neighborX >= 0 && neighborX < canvasSize
    // postcondition: neighborY >= 0 && neighborY < canvasSize
    // postcondition: neighbors != null
    // postcondition: neighbors.size() == neighborsOffSet.length

    private List<SpecialPoint> getNeighbors(Scent foodScent, Scent anthillScent, List<SpecialPoint> specialPoints) {
        List<SpecialPoint> neighbors = new ArrayList<>();
        int[][] neighborsOffSet = neighborsDirectionOffSet.get(direction);

        for (int[] offset : neighborsOffSet) {
            // calculate the neighbor's coordinates, considering wrapping around the boundaries
            int neighborX = (x + offset[0] + canvasSize) % canvasSize;
            int neighborY = (y + offset[1] + canvasSize) % canvasSize;

           /* double foodStrength = foodScent.getScent(neighborX, neighborY);
            double anthillStrength = anthillScent.getScent(neighborX, neighborY);

            boolean isFood = false;

            FoodSource foodSource = null;
            // check if the neighbor is food source, anthill or obstacle

            boolean isObstacle = false;
            Obstacle obstacle = null;
            boolean isAnthill = false;
*/
            boolean isSpecial = false;
            for (SpecialPoint s : specialPoints){
                if (s.getX() == neighborX && s.getY() == neighborY){
                    neighbors.add(s);
                    isSpecial = true;
                    break;
                }
            }
            if (!isSpecial){
                neighbors.add(new Point(neighborX, neighborY));
            }

            /*for (SpecialPoint s : specialPoints){
                if (s.getX() == neighborX && s.getY() == neighborY && s.getClass() == FoodSource.class) {
                    isFood = true;
                    foodSource = (FoodSource) s;
                    break;
                } else if (s.getX() == neighborX && s.getY() == neighborY && s.getClass() == Obstacle.class) {
                    isObstacle = true;
                    obstacle = (Obstacle) s;
                    break;
                } else if (s.getX() == neighborX && s.getY() == neighborY && s.getClass() == Anthill.class) {
                    isAnthill = true;
                    resetFuel();
                }
            }


            // add neighbor information to the list
            neighbors.add(new SingleFieldInfo(neighborX, neighborY, foodStrength, anthillStrength, isFood, isAnthill, foodSource, obstacle, isObstacle));*/
        }
        return neighbors;
    }

    // precondition: neighborIndex >= 0 && neighborIndex < 8
    // postcondition: direction >= 0 && direction < 360
    private void updateDirectionSorted(int neighborIndex) {
        direction = (90 - (neighborIndex * 45) + 360) % 360;
    }

    // precondition: dx >= -3 && dx <= 3
    // precondition: dy >= -3 && dy <= 3
    // postcondition: direction >= 0 && direction < 360
    private void updateDirection(int dx, int dy) {
        if (dx < -3) dx = -3;
        else if (dx > 3) dx = 3;
        if (dy < -3) dy = -3;
        else if (dy > 3) dy = 3;
        String d = dx + "," + dy;
        direction = updateDirectionMap.get(d);
    }

    // move the ant to a new location
    // precondition: newX and newY are not far off from 0 to canvasSize
    // postcondition: this.x and this.y are within canvasSize
    private void moveTo(int newX, int newY) {
        this.x = newX;
        this.y = newY;

        if (newX >= canvasSize){
            this.x = newX - canvasSize;
        } else if (newX < 0) {
            this.x = newX + canvasSize;
        }
        if (newY >= canvasSize){
            this.y = newY - canvasSize;
        } else if (newY < 0) {
            this.y = newY + canvasSize;
        }
    }

    // precondition: f != null
    // postcondition: f.getFoodAmount >= 0
    public void takeFood(FoodSource f) {
        if (f.getFoodAmount() > 0) {
            f.decreaseFood();
        }
    }

    // precondition: neighbor != null
    // precondition: i >= 0 && i < 0
    // postcondition: (state == State.CARRYING_FOOD && hasFood) || (!neighbor.isFood() && state != State.CARRYING_FOOD)
    private void checkFood(SpecialPoint neighbor, int i) {
        if (neighbor instanceof FoodSource){
            if (((FoodSource) neighbor).getFoodAmount() > 1){
                updateDirectionSorted(i);
                moveTo(neighbor.getX(), neighbor.getY());
                takeFood((FoodSource)neighbor);
                hasFood = true;
                state = State.CARRYING_FOOD;
            }
        }
    }

    private void checkRain() {
        if (Rain.isRaining()) {
            state = State.RAIN;
        }
    }

    /*
    // precondition: n != null:
    private int getPreferenceScore(SpecialPoint n) {
        int[][] neighborsOffSet = neighborsDirectionOffSet.get(direction);
        Map<String, Integer> directionPreference = new HashMap<>();
        directionPreference.put(arrayToString(neighborsOffSet[0]), 0); // left
        directionPreference.put(arrayToString(neighborsOffSet[1]), 1); // up-left
        directionPreference.put(arrayToString(neighborsOffSet[2]), 1); // up
        directionPreference.put(arrayToString(neighborsOffSet[3]), 1); // up-right
        directionPreference.put(arrayToString(neighborsOffSet[4]), 0); // right
        String currentOffSet = (n.getX() - x) + "," + (n.getY() - y);
        return directionPreference.get(currentOffSet);
    }
    */

    private String arrayToString(int[] array) {
        return array[0] + "," + array[1];
    }

    // BAD: lange Methoden, die die Lesbarkeit beeinträchtigen (prozedual)
    // BAD: Verzweigungen in den Methoden, sollte man in mehrere Methoden aufteilen (prozedual)
    // BAD: Verhalten der Ameisen in verschiedenen Zuständen wird in derselben Methode behandelt, das deutet auf einen
    //      niedrigen Klassenzusammenhalt hin. Man könnte es in mehrere Methoden aufteilen.
    // exploration behavior of the ant
    // precondition: The input parameters should not be null.
    // precondition: state != null
    // precondition: strength >= 0
    private void explore(Scent foodScent, Scent anthillScent, List<SpecialPoint> specialPoints) {
        checkRain();

        if (state == State.RAIN){
            return;
        }

        List<SpecialPoint> neighbors = getNeighbors(foodScent, anthillScent, specialPoints);
        Iterator<SpecialPoint> iterator = neighbors.iterator();

        // if neighbor is an obstacle and the ant is not strong enough to overcome it, it's going to be removed from the list
        while (iterator.hasNext()) {
            SpecialPoint n = iterator.next();
            if (n.getClass() == Obstacle.class && ((Obstacle)n).getLevel() > strength) {
                iterator.remove(); // Safely remove the element
            }
        }

        for (int i = 0; i < neighbors.size(); i++) {
            SpecialPoint n = neighbors.get(i);
            int dx = x, dy = y;
            checkFood(n, i);
            if (dx != x || dy != y){
                return;
            }

            // choose a neighbor with a strong food scent for foraging
            if (foodScent.getScent(n.getX(), n.getY()) >= strongScent && state == State.EXPLORING) {
                updateDirectionSorted(i);
                moveTo(n.getX(), n.getY());
                state = State.FORAGING;
                if (neighbors.get(0).getClass() == Obstacle.class && ((Obstacle) n).getLevel() * 0.9 > strength) { strength += .3; }
                return;
            }
        }

        // if there are no strong scents, choose the direction with a weak anthill scent
        SpecialPoint chosenNeighbor;
        for (int i = 0; i < neighbors.size(); i++) {
            neighbors.get(i).calcScentStrength(anthillScent);
        }
        neighbors.sort(Comparator.comparingDouble(SpecialPoint::getScentStrength));
        double weakestAnthillScent = neighbors.get(0).getScentStrength();

        // When there are multiple weak anthill scents (interval: weakest to weakest+0.1), choose a random one preferring going up, up-left and up-right.
        List<SpecialPoint> weakestAnthillScentNeighbors = new ArrayList<>();
        for (SpecialPoint n : neighbors) {
            n.calcScentStrength(anthillScent);
            if (n.getScentStrength() <= weakestAnthillScent + 0.1 ) {
                weakestAnthillScentNeighbors.add(n);
            } else {
                break;
            }
        }
        /*System.out.println(weakestAnthillScentNeighbors.size() + "size");
        System.out.println((int) (Math.random() * weakestAnthillScentNeighbors.size()));*/
        chosenNeighbor = weakestAnthillScentNeighbors.get((int) (Math.random() * weakestAnthillScentNeighbors.size()));

        /*funktioniert mit boundaries nicht -> Andy wird sie aber eh noch entfernen
        //wenn links oder rechts wird nochmal random ausgewählt
        if (getPreferenceScore(chosenNeighbor) == 0) {
            chosenNeighbor = weakestAnthillScentNeighbors.get((int) (Math.random() * weakestAnthillScentNeighbors.size()));
        }*/
        if (chosenNeighbor.getClass() == Obstacle.class && ((Obstacle) chosenNeighbor).getLevel() * 0.9 > strength) { strength += 0.3; }
        updateDirection(chosenNeighbor.getX() - x, chosenNeighbor.getY() - y);
        moveTo(chosenNeighbor.getX(), chosenNeighbor.getY());
    }

    // BAD: redundanter Code (z.B. Entfernen von Nachbarn mit unüberwindbaren Hindernissen in explore, forage und carry) (prozedual)
    // foraging behavior of the ant
    // precondition: The input parameters should not be null.
    // precondition: state != null
    // precondition: strength >= 0
    private void forage(Scent foodScent, Scent anthillScent, List<SpecialPoint> specialPoints) {
        checkRain();
        if (state == State.RAIN){
            return;
        }

        List<SpecialPoint> neighbors = getNeighbors(foodScent, anthillScent, specialPoints);
        Iterator<SpecialPoint> iterator = neighbors.iterator();

        // if neighbor is an obstacle and the ant is not strong enough to overcome it, it's going to be removed from the list
        while (iterator.hasNext()) {
            SpecialPoint n = iterator.next();
            if (n.getClass() == Obstacle.class && ((Obstacle)n).getLevel() > strength) {
                iterator.remove(); // Safely remove the element
            }
        }

        // List<SingleFieldInfo> strongFoodScentNeighbors = new ArrayList<>();

        // if neighbor field is food, move towards it
        for (int i = 0; i < neighbors.size(); i++) {
            SpecialPoint n = neighbors.get(i);
            int dx = x, dy = y;
            checkFood(n, i);
            if (dx != x || dy != y){
                return;
            }

            /*
            if (n.getFoodStrength() > strongScent) {
                strongFoodScentNeighbors.add(n);
            }
            */
        }

        /*

        int maxScentX, maxScentY;

        if (!strongFoodScentNeighbors.isEmpty()) {
            SingleFieldInfo chosenNeighbor = strongFoodScentNeighbors.get((int) (Math.random() * strongFoodScentNeighbors.size()));
            maxScentX = chosenNeighbor.getX();
            maxScentY = chosenNeighbor.getY();
        } else {
            Collections.shuffle(neighbors);
            maxScentX = neighbors.get(0).getX();
            maxScentY = neighbors.get(0).getY();
            weakScentSteps++;
            if (weakScentSteps >= maxWeakSteps) {
                state = State.EXPLORING;
            }
            weakScentSteps = 0;
        }*/


        // sort neighbors based on food scent strength in descending order
        for (int i = 0; i < neighbors.size(); i++) {
            neighbors.get(i).calcScentStrength(foodScent);
        }
        neighbors.sort(Comparator.comparingDouble(SpecialPoint::getScentStrength).reversed());


        // get coordinates of neighbor field with the strongest food scent
        int maxScentX, maxScentY;
        if (Math.random() < .9) {
            maxScentX = neighbors.get(0).getX();
            maxScentY = neighbors.get(0).getY();
            if (neighbors.get(0).getClass() == Obstacle.class && ((Obstacle)neighbors.get(0)).getLevel() * 0.9 > strength) { strength += .3; }
        } else {
            maxScentX = neighbors.get(1).getX();
            maxScentY = neighbors.get(1).getY();
            if (neighbors.get(1).getClass() == Obstacle.class && ((Obstacle)neighbors.get(1)).getLevel() * 0.9 > strength) { strength += .3; }
        }


        // if strongest food scent is weaker, switch to exploring state after "maxWeakSteps" steps
        if (foodScent.getScent(maxScentX, maxScentY) < strongScent) {
            weakScentSteps++;
            if (weakScentSteps >= maxWeakSteps) {
                state = State.EXPLORING;
                weakScentSteps = 0;
            }
        } else {
            weakScentSteps = 0;
        }

        // calculate the direction to move based on the strongest scent
        updateDirection(maxScentX - x, maxScentY - y);
        moveTo(maxScentX, maxScentY);
    }

    // BAD: Zufällige Logik wird verwendet, um Entscheidungen zu treffen, dadurch wird die Vorhersehbarkeit des Ameisenverhaltens beeinträchtigt werden. (prozedual)
    // BAD: Methode enthält das Verhalten für sowohl ältere als auch jüngere Ameisen, was zu einer schlechten Kohäsion führt,
    //      da die Verantwortlichkeiten nicht klar getrennt sind. Man könnte es in mehrere Methoden aufteilen.
    // behavior when the ant is carrying food
    // precondition: The input parameters should not be null.
    // precondition: state != null
    // precondition: strength >= 0
    // precondition: age >= 0
    private void carry(Scent foodScent, Scent anthillScent, List<SpecialPoint> specialPoints) {

        List<SpecialPoint> neighbors = getNeighbors(foodScent, anthillScent, specialPoints);
        Iterator<SpecialPoint> iterator = neighbors.iterator();

        // if neighbor is an obstacle and the ant is not strong enough to overcome it, it's going to be removed from the list
        while (iterator.hasNext()) {
            SpecialPoint n = iterator.next();
            if (n.getClass() == Obstacle.class  && ((Obstacle)n).getLevel() > strength) {
                iterator.remove(); // Safely remove the element
            }
        }

        // Occasionally choose a random direction with a 10% chance

        /*
        if (age >= 12) {
            if (Math.random() < 0.10) {
                switch ((int) (Math.random() * 4)) {
                    case 0:
                        dx += scale;
                        break;  // Move right
                    case 1:
                        dx -= scale;
                        break;  // Move left
                    case 2:
                        dy += scale;
                        break;  // Move down
                    case 3:
                        dy -= scale;
                        break;  // Move up
                }
            } else {
                if (x < specialPoints.get(0).getX()) dx += scale;
                else if (x > specialPoints.get(0).getX()) dx -= scale;

                if (y < specialPoints.get(0).getY()) dy += scale;
                else if (y > specialPoints.get(0).getY()) dy -= scale;
            }
            updateDirection(dx, dy);
            moveTo(x + dx, y + dy);
        } else {
            neighbors.sort(Comparator.comparingDouble(SingleFieldInfo::getAnthillStrength).reversed());

            int maxScentX, maxScentY;
            if (Math.random() < 0.5) {
                if (x < anthill.getX()) dx += scale;
                else if (x > anthill.getX()) dx -= scale;
                if (y < anthill.getY()) dy += scale;
                else if (y > anthill.getY()) dy -= scale;
                updateDirection(dx, dy);
                moveTo(x + dx, y + dy);
            } else {
                if (neighbors.get(0).getAnthillStrength() > 0.1) {
                    if (Math.random() < .9) {
                        maxScentX = neighbors.get(0).getX();
                        maxScentY = neighbors.get(0).getY();
                    } else {
                        maxScentX = neighbors.get(1).getX();
                        maxScentY = neighbors.get(1).getY();
                    }
                } else {
                    Collections.shuffle(neighbors);
                    maxScentX = neighbors.get(0).getX();
                    maxScentY = neighbors.get(0).getY();
                }
                updateDirection(maxScentX - x, maxScentY - y);
                moveTo(maxScentX, maxScentY);
            }
        }
         */

        int dx = 0, dy = 0;
        if (age >= 12) {
            if (Math.random() < 0.30) {
                switch ((int) (Math.random() * 4)) {
                    case 0:
                        dx += scale;
                        break;  // Move right
                    case 1:
                        dx -= scale;
                        break;  // Move left
                    case 2:
                        dy += scale;
                        break;  // Move down
                    case 3:
                        dy -= scale;
                        break;  // Move up
                }
            } else {
                if (x < specialPoints.get(0).getX()) dx += scale;
                else if (x > specialPoints.get(0).getX()) dx -= scale;

                if (y < specialPoints.get(0).getY()) dy += scale;
                else if (y > specialPoints.get(0).getY()) dy -= scale;
            }
            updateDirection(dx, dy);
            moveTo(x + dx, y + dy);
        } else {
            if (visitedFields.size() != 0) {
                Point v = visitedFields.pollFirst();
                updateDirection(v.getX() - x, v.getY() - y);
                moveTo(v.getX(),v.getY());


                     /*
                    for (SingleFieldInfo v : visitedFields) {
                        updateDirection(v.getX()-x,v.getY()-y);
                        moveTo(v.getX(),v.getY());
                        if (x == anthill.getX() && y == anthill.getY() && state != State.NIGHT) {
                            hasFood = false;
                            state = State.FORAGING;
                            direction = (direction + 180) % 360;
                            return;
                    }
                    */

            } else {
                int maxScentX, maxScentY;
                for (int i = 0; i < neighbors.size(); i++) {
                    neighbors.get(i).calcScentStrength(anthillScent);
                }
                if (neighbors.get(0).getScentStrength() > 0.1) {
                    if (Math.random() < .9) {
                        maxScentX = neighbors.get(0).getX();
                        maxScentY = neighbors.get(0).getY();
                        if (neighbors.get(0).getClass() == Obstacle.class && ((Obstacle)neighbors.get(0)).getLevel() * 0.9 > strength) { strength += .3; }
                    } else {
                        maxScentX = neighbors.get(1).getX();
                        maxScentY = neighbors.get(1).getY();
                        if (neighbors.get(1).getClass() == Obstacle.class && ((Obstacle)neighbors.get(1)).getLevel() * 0.9 > strength) { strength += .3; }
                    }
                } else {
                    Collections.shuffle(neighbors);
                    maxScentX = neighbors.get(0).getX();
                    maxScentY = neighbors.get(0).getY();
                    if (neighbors.get(0).getClass() == Obstacle.class && ((Obstacle)neighbors.get(0)).getLevel() * 0.9 > strength) { strength += .3; }
                }
                updateDirection(maxScentX - x, maxScentY - y);
                moveTo(maxScentX, maxScentY);
            }
        }

        if (((Anthill) specialPoints.get(0)).isAntInside(this) && state != State.NIGHT) {
            hasFood = false;
            state = State.FORAGING;
            direction = (direction + 180) % 360;
            visitedFields = new LinkedList<>();
        }
    }


    public int getX() { return x; }

    public int getY() { return y; }

    public int getAge() { return age; }

    public State getState() { return state; }

    public LinkedList<Point> getVisitedFields() { return visitedFields; }

    public boolean hasFood() { return hasFood; }

    public void resetFuel() { this.fuel = maxFuel; }

    public void decayFuel() { if (fuel > 0.5) this.fuel *= 0.995; }

    public void setStateNight() {
        if (this.state != State.NIGHT){
            previousState = this.state;
        }
        this.state = State.NIGHT; }
    public void setStatePrevious() { this.state = previousState; }
}



