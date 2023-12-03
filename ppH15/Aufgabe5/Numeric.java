import java.util.function.DoubleUnaryOperator; // Funktionales Interface: repräsentiert Operation auf einem einzelnen
                                               // double-Operanden, die einen double-Wert returned.

public class Numeric implements DoubleUnaryOperator, Calc<Numeric>, Rated<DoubleUnaryOperator, Numeric>{

    private double value;
    private DoubleUnaryOperator criterion;

    public Numeric(double value) {
        this.value = value;
    }

    // Implementierung von Calc

    // Vorbedingung: 'element' darf nicht null sein.
    // Nachbedingung: Gibt ein neues Numeric-Objekt, das die Summe von 'this' und 'element' entspricht, zurück.
    @Override
    public Numeric sum(Numeric element) {
        return new Numeric(this.value + element.value);
    }

    // Vorbedingung: 'divisor' darf nicht 0 sein.
    // Nachbedingung: Gibt ein neues Numeric-Objekt, das 'this' geteilt durch 'divisor' darstellt, zurück.
    @Override
    public Numeric ratio(int divisor) {
        return new Numeric(this.value / divisor);
    }

    // Vorbedingung: 'element' darf nicht null sein.
    // Nachbedingung: true wird zurückgegeben, wenn der Wert von this größer gleich dem Wert des Parameters ist.
    @Override
    public boolean atLeast(Numeric element) {
        return this.value >= element.value;
    }


    // Implementierung von Rated

    // Vorbedingung: 'p' darf nicht null sein.
    // Nachbedingung: Gibt ein neues Numeric-Objekt zurück, das das Ergebnis der Anwendung von 'p' auf 'this' darstellt.
    @Override
    public Numeric rated(DoubleUnaryOperator p) {
        return new Numeric(p.applyAsDouble(this.value));
    }

    // Nachbedingung: Legt den Parameter p in 'this' ab und künftige Aufrufe von 'rated()' verwenden dieses Kriterium.
    @Override
    public void setCriterion(DoubleUnaryOperator p) {
        this.criterion = p;
    }

    // Vorbedinung: Ein Kriterium muss zuvor mit 'setCriterion()' gesetzt worden sein.
    // Nachbedingung: Gibt ein neues Numeric-Objekt zurück, das das Ergebnis der Anwendung des gesetzen Kriteriums auf
    //                'this' darstellt.
    @Override
    public Numeric rated() {
        if (this.criterion == null) {
            throw new IllegalStateException("Kriterium muss zuerst mit setCriterion() gesetzt werden.");
        }
        return new Numeric(this.criterion.applyAsDouble(this.value));
    }

    @Override
    public double applyAsDouble(double operand) {
        return this.value;
    }
}
