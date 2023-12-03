public class Arena implements FormicariumPart {

    private Compatibility compatibility;

    private final substrateType substrateType;

    // Vorbedingungen:
    // - min-Werte <= max-Werte
    // - minSize, maxSize > 0;
    // - minTemperature > -273
    // - minHumidity und maxHumidity müssen zwischen 0 und 100 sein.
    // - time und maxTime muss einer der 6 Konstanten entsprechen (TIME_HOUR, TIME_DAY, TIME_WEEK, TIME_MONTH, TIME_YEAR oder
    //   TIME_UNLIMITED)
    // - substrateType darf nicht leer oder null sein.
    // Nachbedingung: Erstellt eine Arena-Instanz mit den spezifizierten Umweltparametern und Substrattyp.
    public Arena (double minSize, double maxSize, double minTemperature, double maxTemperature,
                  double minHumidity, double maxHumidity, int time, int maxTime, substrateType substrateType) {
        this.compatibility = new Compatibility(minSize, maxSize, minTemperature, maxTemperature, minHumidity, maxHumidity, time, maxTime);
        this.substrateType = substrateType;
    }

    // Nachbedingung: Gibt eine gültige Instanz von Compatibility zurück, die die Umweltbedingungen der Arena
    // repräsentiert.
    @Override
    public Compatibility compatibility() {
        return this.compatibility;
    }
}
