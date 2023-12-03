public class Thermometer implements Instrument, FormicariumPart{

    private final double minTemperature; // Tiefste messbare Temperatur
    private final double maxTemperature; // Höchste messbare Temperatur

    // Vorbedingung: minTemperature > -273 && maxTemperatur < 300
    // Vorbedingung: minTemperature <= maxTemperature
    public Thermometer(double minTemperature, double maxTemperature) {
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
    }

    // Nachbedingung: Printet einen Text, der angibt, für welche Verwendung das Thermometer ausgelegt ist, abhängig von der Messspannweite.
    @Override
    public void quality() {
        double t = maxTemperature - minTemperature;
        if (minTemperature < 0){
            t = maxTemperature + Math.abs(minTemperature);
        }
        if (t > 200){
            System.out.println("Dieses Instrument ist für eine professionelle Verwendung ausgelegt");
        } else if (t > 100) {
            System.out.println("Dieses Instrument ist für eine semiprofessionelle Verwendung ausgelegt");
        } else
            System.out.println("Dieses Instrument ist für eine gelegentliche Verwendung ausgelegt");
    }

    @Override
    public Compatibility compatibility() {
        return new Compatibility(Integer.MIN_VALUE, Integer.MAX_VALUE, minTemperature, maxTemperature, 0, 100, Compatibility.TIME_UNLIMITED, Compatibility.TIME_UNLIMITED);
    }
}
