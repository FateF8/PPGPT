public class HumidifiedNest extends Nest {
    private double tankVolume;


    public HumidifiedNest(double height, double width, int id, double tankVolume) {
        super(height, width, id);
        this.tankVolume = tankVolume;
    }

    public double getTankVolume() {
        return tankVolume;
    }
}
