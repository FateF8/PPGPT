import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class CompositeFormicarium extends Formicarium {
    private List<FormicariumPart> parts;

    // Vorbedingung:
    // - Die übergebene Compatibility-Instanz darf nicht null sein
    // - Die Liste von Parts darf nicht null sein und sollte mindestens einen Part enthalten.
    // Nachbedingung: Ein Formicarium mit spezifizierter Kompatibilität wird erstellt.
    // Invariante: Bestandteile können hinzugefügt oder entfernt werden, die Kompatibilität bleibt jedoch unverändert.
    public CompositeFormicarium(Compatibility compatibility, List<FormicariumPart> parts) {
        super(compatibility, parts);
        this.parts = new ArrayList<>(parts);
    }

    // Vorbedingung: Das hinzufügende Teil muss mit CompositeFormicarium kompatibel und nicht ident sein.
    // Nachbedingung: Ein part von FormicariumPart wird zum CompositeFormicarium hinzugefügt, wenn es kompatibel ist.
    public void add(FormicariumPart part) throws Exception {
        if (this.compatibility().compatible(part.compatibility()) != null) {
            if (!parts.contains(part)) {
                parts.add(part);
            } else {
                throw new Exception("Part with the same identity already exists!");
            }
        } else {
            throw new Exception("Part is not compatible! (unreachable Exception)");
        }
    }

    // Nachbedingung: Gibt einen Iterator zurück, der die Bestandteile des CompositeFormicariums durchläuft.
    // Nachbedingung: 'remove' des Iterators entfernt den zuletzt von 'next' zurückgegebenen Bestandteil, sofern mehr
    //                 ein Bestandteil vorhanden ist.
    public Iterator<FormicariumPart> iterator() {
        return new Iterator<FormicariumPart>() {
            private int idx = 0;
            private boolean canRemove = false;

            @Override
            public boolean hasNext() {
                return idx < parts.size();
            }

            @Override
            public FormicariumPart next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("Keine weiteren Elemente im Iterator.");
                }
                canRemove = true;
                return parts.get(idx++);
            }

            @Override
            public void remove() {
                if (!canRemove || parts.size() <= 1) {
                    throw new IllegalStateException("Cannot remove last element!");
                }
                parts.remove(--idx);
                canRemove = false;
            }
        };
    }

}
