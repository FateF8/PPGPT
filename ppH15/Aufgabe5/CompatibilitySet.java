import java.util.Iterator;

public class CompatibilitySet<X extends Rated<? super X, R>, R> implements RatedSet<X, X, R> {

    private MyLinkedList<X> itemsX;
    private MyLinkedList<X> itemsP;
    private MyLinkedList<X> itemsXP;
    private MyLinkedList<String> itemsS;

    public CompatibilitySet() {
        this.itemsX = new MyLinkedList<>();
        this.itemsP = new MyLinkedList<>();
        this.itemsXP = new MyLinkedList<>();
        this.itemsS = new MyLinkedList<>();
    }

    // Überprüft, ob ein Element in der Liste list enthalten ist.
    private boolean contains(MyLinkedList<X> list, X element) {
        Iterator<X> iterator = list.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().equals(element)) {
                return true;
            }
        }
        return false;
    }
    public void statistics(){
        StringBuilder out = new StringBuilder();
        Iterator<String> sIter = itemsS.iterator();
        while(sIter.hasNext()){
            out.append(sIter.next() + "\r\n");
        }
        System.out.println(out);
        itemsS.addLast("statistics()");
    }

    // Vorbedingung: x!= null
    // Nachbedingung: x wird dem Container hinzugefügt, wenn es nicht bereits enthalten war.
    @Override
    public void add(X x) {
        if (!contains(itemsX, x)) {
            itemsX.addLast(x);
            itemsXP.addLast(x);
            itemsS.addLast("add(X " + x + ")");
        }
    }

    // Vorbedingung: p != null
    // Nachbedingung: p wird dem Container hinzugefügt, wenn es nicht bereits enthalten war.
    @Override
    public void addCriterion(X p) {
        if (!contains(itemsP, p)) {
            itemsP.addLast(p);
            itemsXP.addLast(p);
            itemsS.addLast("addCriterion(X " + p + ")");
        }
    }

    // Nachbedingung: Gibt einen Iterator zurück, der alle Elemente des Containers, die mit add() hinzugefügt worden sind, durchläuft.
    @Override
    public Iterator<X> iterator() {
        itemsS.addLast("iterator()");
        return itemsX.iterator();
    }

    @Override
    public Iterator<X> iterator(X x, R r) {
        itemsS.addLast("iterator(X " + x + ", R " + r + ")");
        return null;
    }

    @Override
    public Iterator<X> iterator(R r) {
        itemsS.addLast("iterator(R " + r + ")");
        return null;
    }
    // Nachbedingung: Gibt einen Iterator zurück, der alle Elemente des Containers, die mit addCriterion() hinzugefügt worden sind, durchläuft.
    @Override
    public Iterator<X> criterions() {
        itemsS.addLast("criterions()");
        return itemsP.iterator();
    }
    // Nachbedingung: Gibt einen Iterator zurück, der alle Elemente des Containers, die mit add() und addCriterion() hinzugefügt worden sind, durchläuft.
    public Iterator<X> identical(){
        itemsS.addLast("identical()");
        return itemsXP.iterator();
    }

}
