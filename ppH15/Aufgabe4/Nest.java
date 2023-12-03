public class Nest implements FormicariumPart {

    private Compatibility compatibility;

    // Vorbedingungen:
    // - min-Werte <= max-Werte
    // - minSize, maxSize > 0; minHumidity, maxHumidity >= 0;
    // - minTemperature > -273
    // - time, maxTime muss einer der 6 Konstanten entsprechen (TIME_HOUR, TIME_DAY, TIME_WEEK, TIME_MONTH, TIME_YEAR oder
    //   TIME_UNLIMITED)
    // Nachbedingung: Erstellt eine Nest-Instanz mit den spezifizierten Umweltparametern.
    public Nest(double minSize, double maxSize, double minTemperature, double maxTemperature,
                double minHumidity, double maxHumidity, int time, int maxTime) {
        this.compatibility = new Compatibility(minSize, maxSize, minTemperature, maxTemperature,
                minHumidity, maxHumidity, time, maxTime);
    }

    // Nachbedingung: Gibt eine gültige Instanz von Compatibility zurück, die die Umweltbedingungen des Nestes
    // repräsentiert.
    @Override
    public Compatibility compatibility() {
        return this.compatibility;
    }
}
