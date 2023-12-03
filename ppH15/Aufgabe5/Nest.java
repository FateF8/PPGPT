public class Nest implements Part {
    private UsageArea usageArea;
    private double antSize;
    private Quality criterion;

    public Nest(UsageArea usageArea, double antSize) {
        this.usageArea = usageArea;
        this.antSize = antSize;
    }

    // Nachbedingung: Gibt das Fassungsvermögen des Nests zurück
    public double antSize() {
        return antSize;
    }


    // Vorbedingung: p != null
    // Nachbedingung: wenn p kein Nest ist, gibt es die resultierende Qualität beider Teile aus
    @Override
    public Quality rated(Part p) {
        if (p instanceof Nest) {
            return Quality.NOT_SUITABLE;
        }
        return (criterion != null) ? criterion.sum(p.getQuality()) : getQuality().sum(p.getQuality());
    }

    // Vorbedingung: p != null
    @Override
    public void setCriterion(Part p) { criterion = p.getQuality(); }

    // Nachbedingung: Gibt die aktuelle Qualität des Nestes zurück
    @Override
    public Quality rated() {
        return (criterion != null) ? criterion : getQuality();
    }

    // Nachbedingung: Gibt Qualitätsstufe des Nests zurück
    public Quality getQuality() {
        if (usageArea == UsageArea.HOBBY) return Quality.HOBBY;
        if (usageArea == UsageArea.SEMIPROFESSIONAL) return Quality.SEMIPROFESSIONAL;
        if (usageArea == UsageArea.PROFESSIONAL) return Quality.PROFESSIONAL;
        return Quality.NOT_SUITABLE;
    }

    // Enum für den Einsatzbereich vom Nest
    enum UsageArea {
        PROFESSIONAL,
        SEMIPROFESSIONAL,
        HOBBY
    }
}
