import java.util.*;

public class FormicariumSet implements Iterable<FormicariumItem>{

    private Map <FormicariumItem, Integer> set;
    private Iterator<FormicariumItem> iterator;

    public FormicariumSet(){
        set = new HashMap<>();
    }

    public Map<FormicariumItem, Integer> getSet() {
        return set;
    }

    // Vorbedingung: item darf nicht null sein.
    // Nachbedingung: Wenn item schon enthalten ist, dann wird item nicht hinzugefügt und alles bleibt unverändert.
    // Nachbedingung: Wenn das Objekt von item schon vorhanden ist, aber item nicht ident mit vorhandenen Objekten sind,
    //                dann erhöht sich value in der Hashmap und item wird zum dazugehörigen key.
    // Nachbedingung: Wenn das Objekt von item noch nicht vorhanden ist, dann wird item zu einem key, der zum value 1 gemapt wird.
    public void add(FormicariumItem item){
        set.put(item, set.getOrDefault(item, 0) + 1);
    }

    // Nachbedingung: Gibt einen iterator zurück, der über alle Elemente von this iteriert, aber jedes Element nur einmal.
    // Nachbedingung: count() liefert die Anzahl der Elemente zurück, die zuletzt von next aufgerufen worden sind.
    // Nachbedingung: remove() löscht das von next zurück gegebene Element einmal.
    // Nachbedingung: remove(int c) löscht das von next zurück gegebene Element c-mal.
    // Nachbedingung: Es wird eine IllegalStateException geworfen, wenn mehr removed wird als vorhanden ist.
    @Override
    public Iterator<FormicariumItem> iterator() {
        iterator = set.keySet().iterator();
        return new Iterator<FormicariumItem>() {
            int c = 0;
            FormicariumItem lastElement;
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
            @Override
            public FormicariumItem next() {
                lastElement = iterator.next();
                c = set.get(lastElement);
                return lastElement;
            }
            @Override
            public void remove() {
                int r = set.get(lastElement) - 1;
                if (r < 0){
                    throw new IllegalStateException("Cannot remove more elements!");
                }
                set.put(lastElement, r);
                c = r;
                if (r == 0){
                    set.remove(lastElement);
                }
            }
            public void remove(int c) {
                int r = set.get(lastElement) - c;
                if (r < 0){
                    throw new IllegalStateException("Cannot remove so many elements!");
                }
                set.put(lastElement, r);
                c = r;
                if (r == 0){
                    set.remove(lastElement);
                }
            }
            public int count() {
                if (c == 0){
                    throw new IllegalStateException("Cannot count without elements!");
                }
                return c;
            }
        };
    }
}
