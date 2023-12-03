public interface Part extends Rated<Part, Quality> {

    // Nachbedingung: Zeichenkette, die die Beschreibung des Bestandteils darstellt
    String toString();

    // Nachbedingung: gibt Qualitätsstufe des Bestandteils zurück
    // Nachbedingung: result != null
    Quality getQuality();

    // Vorbedingung: p != null
    // Nachbedingung: Gibt ein Bewertungsergebnis von p und aktuelles Objekt zurück
    @Override
    Quality rated(Part p);

}
