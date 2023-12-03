public interface Calc<R> {

    // Vorbedingung: 'element' darf nicht null sein.
    // Nachbedingung: Gibt Summe von 'this' und 'element' zurück, wobei diese nicht unbedingt eine Zahlensumme sein muss.
    R sum(R element);

    // Vorbedingung: 'divisor' darf nicht 0 sein.
    // Nachbedingung: Gibt 'this' geteilt durch 'divisor' zurück, wobei dies nicht unbedingt eine Zahlendivision sein muss.
    R ratio(int divisor);

    // Vorbedingung: 'element' darf nicht null sein.
    // Nachbedingung: true wird zurückgegeben, wenn der Wert von this größer gleich dem Wert des Parameters ist, wobei
    //                der Größenvergleich kein Zahlenvergleich sein muss.
    boolean atLeast(R element);
}
