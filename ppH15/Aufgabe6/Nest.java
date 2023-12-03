public abstract class Nest {
    private static final double DEPTH = 2;
    private double height;
    private double width;
    private int id;
    private double sandClayWeight;
    private double gasConcreteHeight;
    private double gasConcreteWidth;

    public Nest(double height, double width, int id) {
        this.height = height;
        this.width = width;
        this.id = id;
    }

    public void changeFilling(double scWeight) {
        this.sandClayWeight = scWeight;
        this.gasConcreteHeight = 0;
        this.gasConcreteWidth = 0;
    }

    public void changeFilling(double gcHeight, double gcWidth) {
        this.gasConcreteHeight = gcHeight;
        this.gasConcreteWidth = gcWidth;
        this.sandClayWeight = 0;
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public double getSandClayWeight() {
        return sandClayWeight;
    }

    public double getGasConcreteHeight() {
        return gasConcreteHeight;
    }

    public double getGasConcreteWidth() {
        return gasConcreteWidth;
    }

    public double calcVolume() {
        return height * width * DEPTH;
    }

    public int getId() {
        return id;
    }
}
