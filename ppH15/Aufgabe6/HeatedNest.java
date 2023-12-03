public class HeatedNest extends Nest {
    private int heatingPower; // Watt

    public HeatedNest(double height, double width, int id, int heatingPower) {
        super(height, width, id);
        this.heatingPower = heatingPower;
    }

    public int getHeatingPower() {
        return heatingPower;
    }
}
