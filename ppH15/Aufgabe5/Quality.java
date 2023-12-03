public class Quality implements Calc<Quality> {

    private String description;
    private Quality next;

    public static final Quality NOT_SUITABLE = new Quality("nicht für den Einsatz geeignet");
    public static final Quality HOBBY = new Quality("für den Einsatz im Hobbybereich geeignet");
    public static final Quality SEMIPROFESSIONAL = new Quality("für den semiprofessionellen Einsatz geeignet");
    public static final Quality PROFESSIONAL = new Quality("für den professionellen Einsatz geeignet");

    static {
        NOT_SUITABLE.next = HOBBY;
        HOBBY.next = SEMIPROFESSIONAL;
        SEMIPROFESSIONAL.next = PROFESSIONAL;
        PROFESSIONAL.next = null; // Letztes Element in der Kette
    }

    private Quality(String description) {
        this.description = description;
        this.next = null;
    }

    private int getLevel() {
        int level = 0;
        Quality current = NOT_SUITABLE;
        while (current != null) {
            if (current == this) return level;
            level++;
            current = current.next;
        }
        throw new IllegalStateException("Qualitätsstufe nicht gefunden");
    }

    // Vorbedingung: 'element' darf nicht null sein.
    // Nachbedingung: Die schwächere Qualität von 'this' und dem Parameter wird zurückgegeben.
    @Override
    public Quality sum(Quality element) {
        return getLevel() <= element.getLevel() ? this : element;
    }

    // Nachbedingung: 'this' wird, ohne den Parameter zu beachten, zurückgegeben.
    @Override
    public Quality ratio(int divisor) {
        return this;
    }

    // Vorbedingung: 'element' darf nicht null sein.
    // Nachbedingung: Gibt true zurück, wenn das Qualitätslevel von 'this' mindestens auf demselben Level, wie vom
    //                Parameter ist.
    @Override
    public boolean atLeast(Quality element) {
        return getLevel() >= element.getLevel();
    }

    // Nachbedingung: Gibt die Qualitätsbeschreibung des entsprechenden Qualitätslevels als Text zurück.
    @Override
    public String toString() {
        return description;
    }
}
