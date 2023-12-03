public class Scent {
    private double[][] scent; // 2D array with scent values for each cell
    private double decayFactor;
    private int scale = 3;

    // precondition: decayFactor should be between 0 and 1
    // postcondition: scent array should be initialized with the specified dimensions
    // postcondition: decayFactor should be set to the specified value
    public Scent(double decayFactor) {
        this.scent = new double[250 * scale][250 * scale]; // wie bekommen wir canvasSize von Simulation?
        this.decayFactor = decayFactor;
    }

    // precondition: x and y should be within the bounds of the scent array.
    // precondition: amount should be a positive number
    // postcondition: scent value at (x, y) location is increased by the amount.
    public void increaseScent(int x, int y, double amount) { scent[x][y] += amount; }

    // precondition: x and y should be within the bounds of the scent array.
    // postcondition: returns the scents value at (x, y) location
    public double getScent(int x, int y) {
        return scent[x][y];
    }

    // precondition: decayFactor should be between 0 and 1
    // postcondition: all scent values are decreased with a factor of the decayFactor
    public void decayAllScents(double decayFactor) {
        for (int x = 0; x < scent.length; x += scale) {
            for (int y = 0; y < scent[x].length; y += scale) {
                scent[x][y] *= decayFactor;
            }
        }
    }

}
