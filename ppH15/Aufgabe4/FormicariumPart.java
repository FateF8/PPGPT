// Ein Formicarium oder ein möglicher Bestandteil eines Formicariums.
public interface FormicariumPart extends FormicariumItem{

    // Methode, die ein Objekt von Compatibility zurückgibt.
    // Wenn ein Umweltkriterium nicht relevant ist, wird dafür der größtmögliche Wertebereich angenommen.

    // Nachbedingung: Ein Objekt vom Typ Compatibility wird zurückgegeben. Dieses Objekt repräsentiert die
    //                Umweltbedingungen, die für ihn geeignet sind.
    Compatibility compatibility();
}