// <P> Der Typ des Objekts, das für die Bewertung verwendet wird.
// <R> Der Typ des Ergebnisses der Bewertung.
public interface Rated<P, R> {

    // Vorbedingung: p != null
    // Nachbedingung: Gibt ein Bewertungsergebnis von p und aktuelles Objekt zurück
    R rated(P p);

    // Vorbedingung: p != null
    void setCriterion(P p);

    // Nachbedingung: Gibt die aktuelle Qualität des Objekts zurück
    R rated();
}
