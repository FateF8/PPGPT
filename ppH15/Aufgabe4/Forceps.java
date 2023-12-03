public class Forceps implements FormicariumItem, Instrument {
    private final int minSize; // Minimale Größe der Ameisen, für die die Pinzette geeignet ist
    private final int maxSize; // Maximale Größe der Ameisen, für die die Pinzette geeignet ist

    // Vorbedingung: minSize, maxSize > 0
    // Vorbedingung: minSize <= maxSize
    // Nachbedingung: Eine Forceps-Instanz mit den angegebenen Größenbeschränkungen wird erstellt.
    public Forceps(int minSize, int maxSize) {
        this.minSize = minSize;
        this.maxSize = maxSize;
    }

    // Getter-Methoden
    public int getMinSize() {
        return minSize;
    }

    public int getMaxSize() {
        return maxSize;
    }


    // Nachbedingung: Gibt eine Compatibility-Instanz zurück, das die geeigneten Größen der Ameisen für diese Pinzette
    //                beschreibt. Nicht relevante Umweltkriterien haben den größtmöglichen Wertebereich.
    @Override
    public Compatibility compatibility() {
        return new Compatibility(minSize, maxSize, Double.MIN_VALUE, Double.MAX_VALUE, 0, 100, Compatibility.TIME_UNLIMITED, Compatibility.TIME_UNLIMITED);
    }
    // Nachbedingung: Printet einen Text, der angibt, dass das Forceps für die semiprofessionelle Verwendung ausgelegt ist.
    @Override
    public void quality() {
        int d = maxSize - minSize;
        if (d > 100){
            System.out.println("Dieses Instrument ist für eine professionelle Verwendung ausgelegt");
        } else if (d > 50) {
            System.out.println("Dieses Instrument ist für eine semiprofessionelle Verwendung ausgelegt");
        } else
            System.out.println("Dieses Instrument ist für eine gelegentliche Verwendung ausgelegt");
    }
}
