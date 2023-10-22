import codedraw.CodeDraw;

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
        CARRYING_FOOD
    }
    private State state;

    // variables that may need to be adjusted
    private double strongScent = .5;
    private int weakScentSteps = 0;
    private int maxWeakSteps = 10;


    public Ant(int x, int y, int direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.state = State.EXPLORING;

    }

    // move ants and update their states
    public void antsMove(Scent scent, Scent foodScent, List<SpecialPoint> specialPoints) {

        if (state == Ant.State.EXPLORING){
            explore(scent, specialPoints);
            foodScent.increaseScent(x, y, 1);
        } else if (state == Ant.State.FORAGING) {
            forage(scent, specialPoints);
            foodScent.increaseScent(x, y,1);
        } else {
            carry(specialPoints);
            scent.increaseScent(x, y, 1);
        }

            /*

            /*
            int movingDirection = (int) (Math.random() * 5) * 45; // 0, 45, 90, 135, 180; random, needs to be changed
            int newDirection = (a.getDirection() + movingDirection - 90) % 360;
            a.setDirection(newDirection);



            // new position
            int dx = 0, dy = 0;
            switch (newDirection) {
                case 0:    // Facing Right
                    dx = 1;
                    break;
                case 45:   // Facing Top-Right
                    dx = 1;
                    dy = -1;
                    break;
                case 90:   // Facing Top
                    dy = -1;
                    break;
                case 135:  // Facing Top-Left
                    dx = -1;
                    dy = -1;
                    break;
                case 180:  // Facing Left
                    dx = -1;
                    break;
                case 225:  // Facing Bottom-Left
                    dx = -1;
                    dy = 1;
                    break;
                case 270:  // Facing Bottom
                    dy = 1;
                    break;
                case 315:  // Facing Bottom-Right
                    dx = 1;
                    dy = 1;
                    break;
            }
            int newX = antX + dx ;
            int newY = antY + dy;

            if (newX < 0 || newY < 0 || newX > 249 || newY > 249){
                if (newX < 0){
                    newX = cd.getWidth() + newX;
                }
                if (newY < 0){
                    newY = cd.getHeight() + newY;
                }
                if (newX > 249){
                    newX = newX - cd.getWidth();
                }
                if (newY > 249){
                    newY = newY - cd.getHeight();
                }
            }

            a.setX(newX);
            a.setY(newY);

*/

        // hasFood
        for (SpecialPoint s : specialPoints) {
            if (s.getClass() == FoodSource.class && x * scale == s.getX() && y * scale == s.getY() && !hasFood) {
                pickUpFood();
            }
            if (s.getClass() == Anthill.class && x * scale == s.getX() && y * scale == s.getY() && hasFood){
                dropFood();
            }
        }
    }


    // get neighbors for the ant to explore
    private Map<String, SingleFieldInfo> getNeighbors (Scent scent, List<SpecialPoint> specialPoints) {
        Map<String, SingleFieldInfo> neighbors = new HashMap<>();


        // define offsets for neighboring cells in different directions
        int[][] neighborsOffSet0 = {{0, -scale}, {scale, -scale}, {scale, 0}, {scale, scale}, {0, scale}};
        int[][] neighborsOffSet45 = {{-scale, -scale}, {0, -scale}, {scale, -scale}, {scale, 0}, {scale, scale}};
        int[][] neighborsOffSet90 = {{-scale, 0}, {-scale, -scale}, {0, -scale}, {scale, -scale}, {scale, 0}};
        int[][] neighborsOffSet135 = {{-scale, scale}, {-scale, 0}, {-scale, -scale}, {0, -scale}, {scale, -scale}};
        int[][] neighborsOffSet180 = {{0, scale}, {-scale, scale}, {-scale, 0}, {-scale, -scale}, {0, -scale}};
        int[][] neighborsOffSet225 = {{scale, scale}, {0, scale}, {-scale, scale}, {-scale, 0}, {-scale, -scale}};
        int[][] neighborsOffSet270 = {{scale, 0}, {scale, scale}, {0, scale}, {-scale, scale}, {-scale, 0}};
        int[][] neighborsOffSet315 = {{scale, -scale}, {scale, 0}, {scale, scale}, {0, scale}, {-scale, scale}};

        // determine the offset array based on the current direction of the ant
        int[][] neighborsOffSet = neighborsOffSet0;

        switch(direction){
            case 0:    // Facing Right
                break;
            case 45:   // Facing Top-Right
                neighborsOffSet = neighborsOffSet45;
                break;
            case 90:   // Facing Top
                neighborsOffSet = neighborsOffSet90;
                break;
            case 135:  // Facing Top-Left
                neighborsOffSet = neighborsOffSet135;
                break;
            case 180:  // Facing Left
                neighborsOffSet = neighborsOffSet180;
                break;
            case 225:  // Facing Bottom-Left
                neighborsOffSet = neighborsOffSet225;
                break;
            case 270:  // Facing Bottom
                neighborsOffSet = neighborsOffSet270;
                break;
            case 315:  // Facing Bottom-Right
                neighborsOffSet = neighborsOffSet315;
                break;
        }

        // calculate the neighboring coordinates, taking into account the boundaries
        for (int[] offset : neighborsOffSet) {
            // calculate the neighbor's coordinates, considering wrapping around the boundaries
            int neighborX = (x + offset[0]) ;
            if (neighborX < 0){neighborX = canvasSize + neighborX;}
            if (neighborX > canvasSize - 1){neighborX = neighborX - canvasSize;}
            int neighborY = (y + offset[1]);
            if (neighborY < 0){neighborY = canvasSize + neighborY;}
            if (neighborY > canvasSize - 1){neighborY = neighborY - canvasSize;}

            double scentStrength = scent.getScent(neighborX, neighborY);
            boolean isFood = false;
            boolean isAnthill = false;

            // check if the neighbor contains food
            for (SpecialPoint s : specialPoints) {
                if (s.getClass() == FoodSource.class && s.getX() == neighborX && s.getY() == neighborY) {
                    isFood = true;
                    break;
                }
                if (s.getClass() == Anthill.class && s.getX() == neighborX && s.getY() == neighborY) {
                    isAnthill = true;
                    break;
                }
            }


            // add neighbor information to the map with coordinates as the key
            String coordinates = neighborX + "," + neighborY;
            neighbors.put(coordinates, new SingleFieldInfo(neighborX, neighborY, scentStrength, isFood, isAnthill));
        }
        return neighbors;
    }

    // move the ant to a new location
    private void moveTo (int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }

    // exploration behavior of the ant
    private void explore(Scent scent, List<SpecialPoint> specialPoints) {
        Map<String, SingleFieldInfo> neighborsMap = getNeighbors(scent, specialPoints);
        List<SingleFieldInfo> neighbors = new ArrayList<>(neighborsMap.values());

        int count = 3;

        for (SingleFieldInfo n : neighbors) {
            count--;
            if (n.isFood()) {
                hasFood = true;
                state = State.CARRYING_FOOD;
                direction = (direction - (count * 45)) % 360;
                moveTo(n.getX(), n.getY());
                return;
            }

            // choose a neighbor with a strong scent for foraging
            if (n.getScentStrength() >= strongScent) {
                state = State.FORAGING;
                direction = (direction - (count * 45)) % 360;
                moveTo(n.getX(), n.getY());
                return;
            }

            // if there are no strong scents, choose a random direction
            Collections.shuffle(neighbors);
            moveTo(neighbors.get(0).getX(), neighbors.get(0).getY());
        }
        /*
        double random = Math.random();
        int multiplier;
        if (random < 0.1){
            multiplier = 0;
        } else if (random < 0.3) {
            multiplier = 1;
        } else if (random < 0.7) {
            multiplier = 2;
        } else if (random < 0.9) {
            multiplier = 3;
        } else multiplier = 4;

         */
    }

    // foraging behavior of the ant
    private void forage(Scent scent, List<SpecialPoint> specialPoints) {
        Map<String, SingleFieldInfo> neighborsMap = getNeighbors(scent, specialPoints);
        List<SingleFieldInfo> neighbors = new ArrayList<>(neighborsMap.values());

        int count = 3;

        // if neighbor field is food, move towards it
        for(SingleFieldInfo n : neighbors) {
            if(n.isFood()) {
                hasFood = true;
                state = State.CARRYING_FOOD;
                direction = (direction + 180) % 360;
                moveTo(n.getX(), n.getY());
                return;
            }
        }

        // sort neighbors based on scent strength in descending order
        Comparator<SingleFieldInfo> scentComparator = new Comparator<SingleFieldInfo>() {
            @Override
            public int compare(SingleFieldInfo field1, SingleFieldInfo field2) {
                return Integer.compare((int) (field1.getScentStrength()*100), (int) (field2.getScentStrength()*100));
            }
        };
        Collections.sort(neighbors, scentComparator.reversed());

        // get coordinates of neighbor field with strongest scent
        int maxScentX = neighbors.get(0).getX(), maxScentY = neighbors.get(0).getY();

        // if strongest scent is weaker, switch to exploring state after 10 steps
        if (scent.getScent(maxScentX, maxScentY) < strongScent) {
            weakScentSteps++;
            if (weakScentSteps >= maxWeakSteps) {
                state = State.EXPLORING;
                weakScentSteps = 0;
            }
        }
        int dx = maxScentX - x;
        int dy = maxScentY - y;

        // calculate the direction to move based on the strongest scent
        if (dx == -1){
            if (dy == -1){
                setDirection(135);
            } else if (dy == 0) {
                setDirection(180);
            } else setDirection(225);
        } else if (dx == 0) {
            if (dy == -1){
                setDirection(90);
            } else if (dy == 1) {
                setDirection(270);
            }
        } else if (dx == 1){
            if (dy == -1){
                setDirection(45);
            } else if (dy == 0) {
                setDirection(0);
            } else setDirection(315);
        }

        moveTo(maxScentX,maxScentY);

    }

    // behavior when the ant is carring food
    private void carry(List<SpecialPoint> specialPoints) {


        // Occasionally choose a random direction with a 10% chance
        if (Math.random() < 0.10) {
            int randomDirection = (int) (Math.random() * 4);
            switch (randomDirection) {
                case 0:
                    x += scale;
                    break;  // Move right
                case 1:
                    x -= scale;
                    break;  // Move left
                case 2:
                    y += scale;
                    break;  // Move down
                case 3:
                    y -= scale;
                    break;  // Move up
            }
        } else {
            if (x < specialPoints.get(0).getX()) x += scale;
            else if (x > specialPoints.get(0).getX()) x -= scale;

            if (y < specialPoints.get(0).getY()) y += scale;
            else if (y > specialPoints.get(0).getY()) y -= scale;
        }

        if (x == specialPoints.get(0).getX() && y == specialPoints.get(0).getY()) {
            hasFood = false;
            state = State.FORAGING;
        }
    }

    public int getX() { return x; }

    public void setX(int x) { this.x = x; }

    public int getY() { return y; }

    public void setY(int y) { this.y = y; }

    public void setDirection(int direction) { this.direction = direction; }

    public int getDirection() { return direction; }

    public boolean hasFood() { return hasFood; }

    public void pickUpFood() { hasFood = true; }

    public void dropFood() { hasFood = false; }

    public State getState() { return state; }

    public void setState(State state) { this.state = state; }
}



