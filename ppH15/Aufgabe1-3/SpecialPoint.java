import codedraw.CodeDraw;

public interface SpecialPoint {
    //„STYLE: objektorientierte Programmierung, das Interface schafft einen Zusammenhang zwischen ähnlichen Klassen und ermöglicht das Arbeiten mit Untertypbeziehungen.“
    int getX();

    int getY();

    // Zeichnet in CodeDraw cd this
    void draw(CodeDraw cd, int scale);

    void calcScentStrength(Scent scent);

    double getScentStrength();

}
