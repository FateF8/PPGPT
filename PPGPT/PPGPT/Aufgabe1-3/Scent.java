public class Scent {
    private double[][] scent; // 2D array with scent values for each cell
    private double decayFactor;
    private int scale = 3;

    public Scent(double decayFactor) {
        this.scent = new double[250 * scale][250 * scale]; // wie bekommen wir canvasSize von Simulation?
        this.decayFactor = decayFactor;
    }

    public void increaseScent(int x, int y, double amount) { scent[x][y] += amount; }

    public double getScent(int x, int y) {
        return scent[x][y];
    }

    public void decayAllScents() {
        for (int x = 0; x < scent.length; x += scale) {
            for (int y = 0; y < scent[x].length; y += scale) {
                scent[x][y] *= decayFactor;
            }
        }
    }

}
