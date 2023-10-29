import java.util.*;

public class Ant {

    private int scale = 3; // muss mit Simulation verknüpft werden
    private int canvasSize = 250 * scale;
    private int canvasCenter = canvasSize / 2;
    private int x, y;
    private int direction; // 0, 45, 90, 135, 180, 225, 270, 315
    private boolean hasFood;

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
    private int maxWeakSteps = 10;
    private double fuel;
    private double maxFuel = 3;
    private int age;
    private double strength; // Strength is between [0;70)


    public Ant(int x, int y, int direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.state = State.EXPLORING;
        initialiseNeighborsOffSet();
        initialiseUpdateDirection();
        this.age = (int) (Math.random() * 24);
        this.fuel = maxFuel;
        this.strength = Math.random() * 70;
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
        updateDirectionMap.put(scale + "," + scale, 315);// Down-Right
        updateDirectionMap.put("0,0", direction);
    }

    // move ants and update their states
    public void antsMove(Scent foodScent, Scent anthillScent, List<FoodSource> foodSources, Anthill anthill, List<Obstacle> obstacles) {

        if (state == State.EXPLORING) {
            explore(foodScent, anthillScent, foodSources, anthill, obstacles);
            anthillScent.increaseScent(x, y, fuel);
            decayFuel();
        } else if (state == State.FORAGING) {
            forage(foodScent, anthillScent, foodSources, anthill, obstacles);
            anthillScent.increaseScent(x, y, fuel);
            decayFuel();
        } else if (state == State.CARRYING_FOOD) {
            carry(foodScent, anthillScent, foodSources, anthill, obstacles);
            foodScent.increaseScent(x, y, 1);
        } else if (state == State.RAIN){
            carry(foodScent, anthillScent, foodSources, anthill, obstacles);
        } else {
            carry(foodScent, anthillScent, foodSources, anthill, obstacles);
            if (previousState == State.CARRYING_FOOD){
                foodScent.increaseScent(x, y, 1);
            } else {
                anthillScent.increaseScent(x, y, fuel);
                decayFuel();
            }
        }
    }

    // get neighbors for the ant to explore
    private List<SingleFieldInfo> getNeighbors(Scent foodScent, Scent anthillScent, List<FoodSource> foodSources, Anthill anthill, List<Obstacle> obstacles) {
        List<SingleFieldInfo> neighbors = new ArrayList<>();
        int[][] neighborsOffSet = neighborsDirectionOffSet.get(direction);

        for (int[] offset : neighborsOffSet) {
            // calculate the neighbor's coordinates, considering wrapping around the boundaries
            int neighborX = (x + offset[0] + canvasSize) % canvasSize;
            int neighborY = (y + offset[1] + canvasSize) % canvasSize;

            double foodStrength = foodScent.getScent(neighborX, neighborY);
            double anthillStrength = anthillScent.getScent(neighborX, neighborY);

            boolean isFood = false;

            FoodSource foodSource = null;
            // check if the neighbor is food source, anthill or obstacle
            for (FoodSource f : foodSources) {
                if (f.getX() == neighborX && f.getY() == neighborY) {
                    isFood = true;
                    foodSource = f;
                    break;
                }
            }

            boolean isObstacle = false;
            Obstacle obstacle = null;
            for(Obstacle o : obstacles) {
                if (o.getX() == neighborX && o.getY() == neighborY) {
                    isObstacle = true;
                    obstacle = o;
                    break;
                }
            }

            boolean isAnthill = (anthill.getX() == neighborX && anthill.getY() == neighborY);
            if (isAnthill) {
                resetFuel();
            }

            // add neighbor information to the list
            neighbors.add(new SingleFieldInfo(neighborX, neighborY, foodStrength, anthillStrength, isFood, isAnthill, foodSource, obstacle, isObstacle));
        }
        return neighbors;
    }

    private void updateDirectionSorted(int neighborIndex) {
        direction = (90 - (neighborIndex * 45) + 360) % 360;
    }

    private void updateDirection(int dx, int dy) {
        if (dx < -3) dx = -3;
        else if (dx > 3) dx = 3;
        if (dy < -3) dy = -3;
        else if (dy > 3) dy = 3;
        String d = dx + "," + dy;
         direction = updateDirectionMap.get(d);
    }

    // move the ant to a new location
    private void moveTo(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }

    private void takeFood(FoodSource f) {
        if (f.getFoodAmount() > 0) {
            f.decreaseFood();
        }
    }

    private void checkFood(SingleFieldInfo neighbor, int i) {
        if (neighbor.isFood() && neighbor.getFoodSource().getFoodAmount() > 1) {
            updateDirectionSorted(i);
            moveTo(neighbor.getX(), neighbor.getY());
            takeFood(neighbor.getFoodSource());
            hasFood = true;
            state = State.CARRYING_FOOD;
            return;
        }
    }

    private void checkRain() {
        if (Rain.isRaining()) {
            state = State.RAIN;
        }
    }


    private int getPreferenceScore(SingleFieldInfo n) {
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

    private String arrayToString(int[] array) {
        return array[0] + "," + array[1];
    }

    // exploration behavior of the ant
    private void explore(Scent foodScent, Scent anthillScent, List<FoodSource> foodSources, Anthill anthill, List<Obstacle> obstacles) {
        checkRain();
        if (state == State.RAIN){
            return;
        }

        List<SingleFieldInfo> neighbors = getNeighbors(foodScent, anthillScent, foodSources, anthill, obstacles);
        Iterator<SingleFieldInfo> iterator = neighbors.iterator();

        // if neighbor is an obstacle and the ant is not strong enough to overcome it, it's going to be removed from the list
        while (iterator.hasNext()) {
            SingleFieldInfo n = iterator.next();
            if (n.getObstacle() != null && n.isObstacle() && n.getObstacle().getLevel() > strength) {
                iterator.remove(); // Safely remove the element
            }
        }


        for (int i = 0; i < neighbors.size(); i++) {
            SingleFieldInfo n = neighbors.get(i);
            int dx = x, dy = y;
            checkFood(n, i);
            if (dx != x || dy != y){
                return;
            }

            // choose a neighbor with a strong food scent for foraging
            if (n.getFoodStrength() >= strongScent && state == State.EXPLORING) {
                updateDirectionSorted(i);
                moveTo(n.getX(), n.getY());
                state = State.FORAGING;
                return;
            }
        }

        // if there are no strong scents, choose the direction with a weak anthill scent
        SingleFieldInfo chosenNeighbor;
        neighbors.sort(Comparator.comparingDouble(SingleFieldInfo::getAnthillStrength));
        double weakestAnthillScent = neighbors.get(0).getAnthillStrength();

        // When there are multiple weak anthill scents (interval: weakest to weakest+0.1), choose a random one preferring going up, up-left and up-right.
        List<SingleFieldInfo> weakestAnthillScentNeighbors = new ArrayList<>();
        for (SingleFieldInfo n : neighbors) {
            if (n.getAnthillStrength() <= weakestAnthillScent + 0.1 ) {
                weakestAnthillScentNeighbors.add(n);
            } else {
                break;
            }
        }

        chosenNeighbor = weakestAnthillScentNeighbors.get((int) (Math.random() * weakestAnthillScentNeighbors.size()));

        //TODO: funktioniert mit boundaries nicht -> Andy wird sie aber eh noch entfernen
        // wenn links oder rechts wird nochmal random ausgewählt
       /* if (getPreferenceScore(chosenNeighbor) == 0) {
            chosenNeighbor = weakestAnthillScentNeighbors.get((int) (Math.random() * weakestAnthillScentNeighbors.size()));
        }*/

        updateDirection(chosenNeighbor.getX() - x, chosenNeighbor.getY() - y);
        moveTo(chosenNeighbor.getX(), chosenNeighbor.getY());
    }

    // foraging behavior of the ant
    private void forage(Scent foodScent, Scent anthillScent, List<FoodSource> foodSources, Anthill anthill, List<Obstacle> obstacles) {
        checkRain();
        if (state == State.RAIN){
            return;
        }

        List<SingleFieldInfo> neighbors = getNeighbors(foodScent, anthillScent, foodSources, anthill, obstacles);
        Iterator<SingleFieldInfo> iterator = neighbors.iterator();

        // if neighbor is an obstacle and the ant is not strong enough to overcome it, it's going to be removed from the list
        while (iterator.hasNext()) {
            SingleFieldInfo n = iterator.next();
            if (n.getObstacle() != null && n.isObstacle() && n.getObstacle().getLevel() > strength) {
                iterator.remove(); // Safely remove the element
            }
        }

        // List<SingleFieldInfo> strongFoodScentNeighbors = new ArrayList<>();

        // if neighbor field is food, move towards it
        for (int i = 0; i < neighbors.size(); i++) {
            SingleFieldInfo n = neighbors.get(i);
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
        neighbors.sort(Comparator.comparingDouble(SingleFieldInfo::getFoodStrength).reversed());

        // get coordinates of neighbor field with the strongest food scent
        int maxScentX, maxScentY;
        if (Math.random() < .9) {
            maxScentX = neighbors.get(0).getX();
            maxScentY = neighbors.get(0).getY();
        } else {
            maxScentX = neighbors.get(1).getX();
            maxScentY = neighbors.get(1).getY();
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


    // behavior when the ant is carrying food
    private void carry(Scent foodScent, Scent anthillScent, List<FoodSource> foodSources, Anthill anthill, List<Obstacle> obstacles) {

        List<SingleFieldInfo> neighbors = getNeighbors(foodScent, anthillScent, foodSources, anthill, obstacles);
        Iterator<SingleFieldInfo> iterator = neighbors.iterator();

        // if neighbor is an obstacle and the ant is not strong enough to overcome it, it's going to be removed from the list
        while (iterator.hasNext()) {
            SingleFieldInfo n = iterator.next();
            if (n.getObstacle() != null && n.isObstacle() && n.getObstacle().getLevel() > strength) {
                iterator.remove(); // Safely remove the element
            }
        }

        // Occasionally choose a random direction with a 10% chance
        int dx = 0, dy = 0;
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
                if (x < anthill.getX()) dx += scale;
                else if (x > anthill.getX()) dx -= scale;

                if (y < anthill.getY()) dy += scale;
                else if (y > anthill.getY()) dy -= scale;
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
        if (x == anthill.getX() && y == anthill.getY()) {
            hasFood = false;
            state = State.FORAGING;
            direction = (direction + 180) % 360;
        }
    }

    public int getX() { return x; }

    public int getY() { return y; }

    public int getAge() { return age; }

    public void resetFuel() { this.fuel = maxFuel; }

    public void decayFuel() { if (fuel > 0.5) this.fuel *= 0.99; }

    public void setStateNight() { previousState = this.state; this.state = State.NIGHT; }
    public void setStatePrevious() { this.state = previousState; }
}



