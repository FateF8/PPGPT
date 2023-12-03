import java.util.Iterator;

public interface RatedSet<X extends Rated<? super P, R>, P, R> extends java.lang.Iterable<X>{

    // Vorbedingung: x!= null
    // Nachbedingung: x wird dem Container hinzugefügt, wenn es nicht bereits enthalten war.
    void add(X x);

    // Vorbedingung: p != null
    // Nachbedingung: p wird dem Container hinzugefügt, wenn es nicht bereits enthalten war.
    void addCriterion(P p);

    // Nachbedingung: Gibt einen Iterator zurück, der alle Elemente des Containers, die mit add() hinzugefügt worden sind, durchläuft.
    @Override
    Iterator<X> iterator();

    Iterator<X> iterator(P p, R r);

    Iterator<X> iterator(R r);

    // Nachbedingung: Gibt einen Iterator zurück, der alle Elemente des Containers, die mit addCriterion() hinzugefügt worden sind, durchläuft.
    Iterator<P> criterions();

}
