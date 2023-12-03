public class AntFarm extends Nest {
    private final substrateType substrateType;
    private double plateDistance;

    // Vorbedingungen:
    // - minSize, maxSize > 0;
    // - minHumidity und maxHumidity müssen zwischen 0 und 100 sein.
    // - time und maxTime muss einer der 6 Konstanten entsprechen (TIME_HOUR, TIME_DAY, TIME_WEEK, TIME_MONTH, TIME_YEAR oder
    //   TIME_UNLIMITED)
    // - substrateType darf nicht leer oder null sein.
    // - plateDistance > 0
    // Nachbedingung: Erstellt eine AntFarm-Instanz mit den spezifizierten Umweltparametern, Substrattyp und
    //                plateDistance.
    public AntFarm(double minSize, double maxSize, double minTemperature, double maxTemperature,
                   double minHumidity, double maxHumidity, int time, int maxTime, substrateType substrateType, double plateDistance) {
        super(minSize, maxSize, minTemperature, maxTemperature, minHumidity, maxHumidity, time, maxTime);
        this.substrateType = substrateType;
        this.plateDistance = plateDistance;
    }

    // Nachbedingung: Gibt eine gültige Instanz von Compatibility zurück, die die Umweltbedingungen der AntFarm
    // repräsentiert.
    @Override
    public Compatibility compatibility() {
        return super.compatibility();
    }
}
