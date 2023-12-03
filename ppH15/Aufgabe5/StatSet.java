import java.util.Iterator;

public class StatSet<X extends Rated<? super P, R>, P, R> implements RatedSet<X, P, R>{

    private MyLinkedList<X> x;
    private MyLinkedList<P> p;
    private MyLinkedList<String> s;

    public StatSet() {
        this.x = new MyLinkedList<X>();
        this.p = new MyLinkedList<P>();
        this.s = new MyLinkedList<>();
    }
    // Überprüft, ob ein Element vom Typ X in der Liste list enthalten ist.
    private boolean containsX(MyLinkedList<X> list, X element) {
        Iterator<X> iterator = list.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().equals(element)) {
                return true;
            }
        }
        return false;
    }
    // Überprüft, ob ein Element vom Typ P in der Liste list enthalten ist.
    private boolean containsP(MyLinkedList<P> list, P element) {
        Iterator<P> iterator = list.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().equals(element)) {
                return true;
            }
        }
        return false;
    }
    // Gibt alle verwendeten Methoden, die in diesem Objekt verwendet worden sind, auf der Konsole zurück.
    public void statistics(){
        StringBuilder out = new StringBuilder();
        Iterator<String> sIter = s.iterator();
        while(sIter.hasNext()){
            out.append(sIter.next() + "\r\n");
        }
        System.out.println(out);
        s.addLast("statistics()");
    }
    // Vorbedingung: x!= null
    // Nachbedingung: x wird dem Container hinzugefügt, wenn es nicht bereits enthalten war.
    @Override
    public void add(X x) {
        if (!containsX(this.x, x)) {
            this.x.addLast(x);
            s.addLast("add(X " + x + ")");
        }
    }
    // Vorbedingung: p != null
    // Nachbedingung: p wird dem Container hinzugefügt, wenn es nicht bereits enthalten war.
    @Override
    public void addCriterion(P p) {
        if (!containsP(this.p, p)) {
            this.p.addLast(p);
            s.addLast("addCriterion(P " + p + ")");
        }
    }
    // Nachbedingung: Gibt einen Iterator zurück, der alle Elemente des Containers, die mit add() hinzugefügt worden sind, durchläuft.
    @Override
    public Iterator<X> iterator() {
        s.addLast("iterator()");
        return x.iterator();
    }

    @Override
    public Iterator<X> iterator(P p, R r) {
        s.addLast("iterator(P " + p + ", R " + r + ")");
        return null;
    }

    @Override
    public Iterator<X> iterator(R r) {
        s.addLast("iterator(R " + r + ")");
        return null;
    }
    // Nachbedingung: Gibt einen Iterator zurück, der alle Elemente des Containers, die mit addCriterion() hinzugefügt worden sind, durchläuft.
    @Override
    public Iterator<P> criterions() {
        s.addLast("criterions()");
        return p.iterator();
    }
}
