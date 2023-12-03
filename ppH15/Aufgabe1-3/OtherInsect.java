public class OtherInsect {
    private int x;
    private int y;
    private final int spawnX;
    private final int spawnY;
    private boolean hasEaten = false;

    // precondition: x >= 0
    // precondition: y >= 0
    // postcondition: insect should be created with valid x, y, spawnX, and spawnY
    public OtherInsect(int x, int y) {
        this.x = x;
        this.y = y;
        this.spawnX = x;
        this.spawnY = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSpawnX() {
        return spawnX;
    }

    public int getSpawnY() {
        return spawnY;
    }

    public boolean isHasEaten() {
        return hasEaten;
    }

    public void setHasEaten(boolean hasEaten) {
        this.hasEaten = hasEaten;
    }

    // precondition: x >= 0
    // precondition: y >= 0
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
