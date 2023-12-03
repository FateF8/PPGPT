public class Compatibility {

    // Invarianten:
    // - Die minimalen Werte der Parameter dürfen die entsprechenden maximalen Werte nicht überschreiten (size,
    //   temperatur, humidity, time).
    // - time und maxTime müssen einer der 6 definierten Konstanten entsprechen (TIME_HOUR, TIME_DAY, TIME_WEEK, ...).

    // Umweltparameter
    private double minSize, maxSize; // Geeignete Größe der Ameisen in mm
    private double minTemperature, maxTemperature; // Temperatur in °C
    private double minHumidity, maxHumidity; // relative Luftfeuchtigkeit in %
    private int time, maxTime; // längstmögliche Dauer der Ameisenhaltung
    // Zeitperioden als Integer Konstante
    public static final int TIME_HOUR = 1;
    public static final int TIME_DAY = 2;
    public static final int TIME_WEEK = 3;
    public static final int TIME_MONTH = 4;
    public static final int TIME_YEAR = 5;
    public static final int TIME_UNLIMITED = 6;

    // Vorbedingung:
    // - minSize, maxSize > 0;
    // - minHumidity und maxHumidity müssen zwischen 0 und 100 sein.
    // - time, maxTime muss einer der 6 Konstanten entsprechen (TIME_HOUR, TIME_DAY, TIME_WEEK, TIME_MONTH,
    //   TIME_YEAR oder TIME_UNLIMITED)
    // Nachbedingung: Erstellt eine Instanz von Compatibility mit den angegebenen Umweltparametern und Zeitbeschränkungen.
    public Compatibility(double minSize, double maxSize, double minTemperature, double maxTemperature, double minHumidity, double maxHumidity, int time, int maxTime) {
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
        this.minHumidity = minHumidity;
        this.maxHumidity = maxHumidity;
        this.time = time;
        this.maxTime = maxTime;
    }

    // Vorbedingung: 'other' darf nicht null sein.
    // Nachbedingung: Gibt eine neue Instanz von Compatibility zurück, die die gemeinsamen Parameter entsprechen. Wenn
    //                keine Kompatibilität besteht, wird eine Ausnahme ausgelöst.
    public Compatibility compatible(Compatibility other) throws Exception {
        if (this.minSize > other.maxSize || this.maxSize < other.minSize ||
            this.minTemperature > other.maxTemperature || this.maxTemperature < other.minTemperature ||
            this.minHumidity > other.maxHumidity || this.maxHumidity < other.minHumidity) {
            throw new Exception("Die Umweltbedingungen sind nicht kompatibel!");
        }

        int newTime = Math.min(this.time, other.time);
        int newMaxTime = Math.min(this.maxTime, other.maxTime);

        double newMinSize = Math.max(this.minSize, other.minSize);
        double newMaxSize = Math.min(this.maxSize, other.maxSize);
        double newMinTemperature = Math.max(this.minTemperature, other.minTemperature);
        double newMaxTemperature = Math.min(this.maxTemperature, other.maxTemperature);
        double newMinHumidity = Math.max(this.minHumidity, other.minHumidity);
        double newMaxHumidity = Math.min(this.maxHumidity, other.maxHumidity);

        return new Compatibility(newMinSize, newMaxSize, newMinTemperature, newMaxTemperature, newMinHumidity, newMaxHumidity, newTime, newMaxTime);
    }

    // Nachbedingungen: Alle Getter-Methoden geben den aktuellen Wert der Variable im Objekt zurück.
    public double getMinSize() { return minSize; }
    public double getMaxSize() { return maxSize; }
    public double getMinTemperature() { return minTemperature; }
    public double getMaxTemperature() { return maxTemperature; }
    public double getMinHumidity() { return minHumidity; }
    public double getMaxHumidity() { return maxHumidity; }
    public int getTime() { return time; }
    public int getMaxTime() { return maxTime; }

    // Vorbedingung: time <= maxTime
    // Nachbedingung: time wird, wenn die Vorbedingung erfüllt ist auf den übergebenen Wert gesetzt.
    public void setTime(int time) throws IllegalAccessException {
        if (time <= this.maxTime) {
            this.time = time;
        } else {
            throw new IllegalAccessException("Die gesetzte Zeit überschreitet die maximale Zeit!");
        }
    }
}