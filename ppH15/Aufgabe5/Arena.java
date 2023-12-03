public class Arena implements Part {
    private UsageArea usageArea;
    private double volume; // Fassungsvermögen in Liter
    private Quality criterion;

    public Arena(UsageArea usageArea, double volume) {
        this.usageArea = usageArea;
        this.volume = volume;
    }

    // Nachbedinung: Gibt das Volumen der Arena zurück
    public double volume() {
        return volume;
    }

    // Vorbedingung: p != null
    // Nachbedingung: Gibt es die resultierende Qualität beider Teile aus
    @Override
    public Quality rated(Part p) {
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

    // Enum für den Einsatzbereich der Arena
    enum UsageArea {
        PROFESSIONAL,
        SEMIPROFESSIONAL,
        HOBBY
    }
}

