import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

class Formicarium implements FormicariumPart, Iterable<FormicariumPart> {
    private final List<FormicariumPart> parts;
    private Compatibility compatibility;

    // Vorbedingung:
    // - Die übergebene Compatibility-Instanz darf nicht null sein
    // - Die Liste von Parts darf nicht null sein und sollte mindestens einen Part enthalten.
    // Nachbedingung: Ein Formicarium mit spezifizierter Kompatibilität wird erstellt.
    // Invariante: Die Liste der Parts darf nach Initialisierung nicht verändert werden.
    // Veränderliche Formicarien -> CompositeFormicarium
    public Formicarium(Compatibility compatibility, List<FormicariumPart> parts) {
        this.compatibility = compatibility;
        this.parts = new ArrayList<>(parts);
    }

    // Nachbedingung: Gibt eine gültige Instanz von Compatibility zurück, die die Umweltbedingungen des Formicariums
    // repräsentiert.
    @Override
    public Compatibility compatibility() {
        return this.compatibility;
    }

    // Nachbedingung: Gibt einen Iterator zurück, der über alle Bestandteile des Formicariums iteriert oder nur 'this'
    //                zurückgibt.
    @Override
    public Iterator<FormicariumPart> iterator() {
        return parts.iterator();
    }
}
