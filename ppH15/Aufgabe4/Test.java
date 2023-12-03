import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Test {

    /*
    Arbeitsverteilung:

    Alle: Überlegung und Zuweisung der Untertypbeziehungen

    Lucy:
    - Implementierung der Klassen: Forceps, Formicarium, FormicariumPart und FormicariumItem
    - Begründung von nicht vorhandenen Untertypbeziehungen

    Julian:
    - Implementierung von AntFarm, Arena, CompositeFormicarium, Compatibility, Formicarium
    - Zusicherungen (außer Instrument, Thermometer, und FormicariumSet)

    Yi:
    - Implementierung von Nest, Thermometer, FormicariumSet und Instrument
    - Einige Zusicherungen
    - Testfälle

    Untertypbeziehungen:

    zwischen Arena, Nest (AntFarm) und Formicarium (CompositeFormicarium) besteht keine Untertypbeziehung, da..
    - eine Arena kein Nest und ein Nest keine Arena ist
    - eine Arena alleine kein Formicarium, da Ameisen dort nicht langfristig überleben
    - ein Formicarium der gesamte Lebensraum mit den dazugehörigen Bestandteilen ist und sie aus einem Nest, einer Arena
      und anderen Bestandteilen bestehen kann
    - ein einzelnes Nest nur kurzfristig als Formicarium genutzt werden kann, man würde noch eine Arena benötigen, damit
      sie überleben können
    ...für AntFarm und CompositeFormicarium gilt die gleiche Begründung, da AntFarm die Beziehung von Nest und
       CompositeFormicarium von Formicarium erbt

    zwischen FormicariumItem und FormicariumSet besteht keine Untertypbeziehung, da..
    - sie beide verschiedene Aufgaben erfüllen; FormicariumItem ist ein einzelnes Element, das entweder ein
      vollständiges Formicarium, ein Bestandteil oder ein Instrument sein kann, und Formicarium stellt
      eine Menge von Formicarien, Bestandteile oder Instrumenten dar

    zwischen Thermometer und Forceps besteht keine Untertypbeziehung, da..
    - beide verschiedene Arten von Instrumente repräsentieren, die unterschiedliche Aufgaben erfüllen;
      beide erben von Instrument, aber Thermometer steht in einer direkten Untertypbeziehung zu FormicariumPart,
      da es auch ein Bestandteil von einem Formicarium sein kann, hingegen besteht zwischen Forceps und FormicariumPart
      keine Untertypbeziehung, da Forceps kein Formicarium oder Bestandteil eines Formicariums sein kann

    zwischen Instrument und FormicariumItem besteht keine Untertypbeziehung, da..
    - Instrumente auch Objekte sein können, die nichts mit dem Formicarium zu tun haben

    Compatible selbst hat keine Untertypbeziehungen, da sie nur eine Sammlung von Eigenschaften eines Objektes darstellt, aber
    nicht das physische Objekt in der realen Welt selbst

    Für Klassen, wo es erkennbar ist, dass keine Beziehung besteht z.B. zwischen Forceps und Arena herrscht auch
    keine Untertypbeziehung.

     */
    public static void main(String[] args) throws Exception {

        System.out.println("Test 'Formicarium':");
        List<FormicariumPart> parts = new ArrayList<>();
        Arena a = new Arena(1,1000, -100, 200, 0, 100, Compatibility.TIME_HOUR, Compatibility.TIME_UNLIMITED, FormicariumItem.substrateType.Dirt);
        Nest n = new Nest(1,1000, -100, 200, 0, 100, Compatibility.TIME_HOUR, Compatibility.TIME_UNLIMITED);
        Nest n2 = new Nest(2,1000, -100, 200, 0, 100, Compatibility.TIME_HOUR, Compatibility.TIME_UNLIMITED);
        parts.add(a);
        parts.add(n);
        Formicarium f = new Formicarium(parts.get(0).compatibility().compatible(parts.get(1).compatibility()), parts);
        parts.add(n2);
        Iterator<FormicariumPart> iter = f.iterator();
        testEquals(iter.next(), a);
        testEquals(iter.next(), n);
        Iterator<FormicariumPart> iter2 = f.iterator();
        int count = 0;
        while(iter2.hasNext()){
            iter2.next();
            count++;
        }
        testEquals(count, 2);
        testEquals(parts.size(), 3);
        System.out.println();

        System.out.println("Test 'FormicariumItem Interface':");
        List<FormicariumItem> list = new ArrayList<>();
        Forceps fo = new Forceps(1,100);
        Thermometer t = new Thermometer(-40, 200);
        Arena a2 = new Arena(1,1000, -100, 200, 0, 100, Compatibility.TIME_HOUR, Compatibility.TIME_UNLIMITED, FormicariumItem.substrateType.Sand);
        AntFarm af = new AntFarm(1,1000, -100, 200, 0, 100, Compatibility.TIME_HOUR, Compatibility.TIME_UNLIMITED, FormicariumItem.substrateType.Gravel, 5);
        list.add(fo);
        list.add(t);
        list.add(a2);
        list.add(af);
        list.add(a);
        list.add(n);
        list.add(f);
        testValue(list.size(), 7);
        System.out.println();

        System.out.println("Test 'Compatibility':");
        Forceps fo2 = new Forceps(1,100);
        Thermometer t2 = new Thermometer(-30, 190);
        testEquals(fo.compatibility().getMinSize(), fo2.compatibility().getMinSize());
        testEquals(fo2.compatibility().getMaxSize(), fo.compatibility().getMaxSize());
        testEquals(fo.compatibility().getMinHumidity(), fo2.compatibility().getMinHumidity());
        System.out.println();

        System.out.println("Failtest 'Compatibility':");
        testEquals(t.compatibility().getMinTemperature(), t2.compatibility().getMinTemperature());
        testEquals(t2.compatibility().getMaxTemperature(), t.compatibility().getMaxTemperature());
        System.out.println();

        System.out.println("Test 'compatible()':");
        Compatibility fo3 = fo.compatibility().compatible(fo2.compatibility());
        Compatibility t3 = t.compatibility().compatible(t2.compatibility());
        testEquals(fo3.getMinSize(), fo.compatibility().getMinSize());
        testEquals(t3.getMinTemperature(), t2.compatibility().getMinTemperature());
        System.out.println();

        System.out.println("Failtest 'compatible()':");
        testValue(t3.getMinTemperature(), t.compatibility().getMinTemperature());
        testValue(t3.getMaxTemperature(), t.compatibility().getMaxTemperature());
        System.out.println();

        System.out.println("Test 'CompositeFormicarium':");
        List<FormicariumPart> parts2 = new ArrayList<>();
        Arena a3 = new Arena(1,999, -100, 200, 0, 100, Compatibility.TIME_HOUR, Compatibility.TIME_UNLIMITED, FormicariumItem.substrateType.Dirt);
        Nest n3 = new Nest(1,999, -100, 200, 0, 100, Compatibility.TIME_HOUR, Compatibility.TIME_UNLIMITED);
        parts2.add(a3);
        parts2.add(n3);
        CompositeFormicarium cf = new CompositeFormicarium(parts2.get(0).compatibility().compatible(parts2.get(1).compatibility()), parts2);
        Iterator<FormicariumPart> iter3 = cf.iterator();
        int count2 = 0;
        while(iter3.hasNext()){
            iter3.next();
            count2++;
        }
        testEquals(count2, 2);
        count2 = 0;
        Nest n4 = new Nest(2,999, -100, 200, 0, 100, Compatibility.TIME_HOUR, Compatibility.TIME_UNLIMITED);
        Nest n5 = new Nest(2,999, 210, 250, 0, 100, Compatibility.TIME_HOUR, Compatibility.TIME_UNLIMITED);
        cf.add(n4);
        Iterator<FormicariumPart> iter4 = cf.iterator();
        while(iter4.hasNext()){
            iter4.next();
            count2++;
        }
        testEquals(count2, 3);

        try{
            cf.add(n4);
        }catch (Exception ex){
            System.out.println("Successful test (exception caught): " + ex);
        }
        try{
            cf.add(n5);
        }catch (Exception ex){
            System.out.println("Successful test (exception caught): " + ex);
        }

        System.out.println("Test 'FormicariumSet':");
        FormicariumSet fs = new FormicariumSet();
        Thermometer t4 = new Thermometer(-50,200);
        Thermometer t5 = new Thermometer(-10,50);
        Thermometer t6 = new Thermometer(-30,100);
        Forceps f3 = new Forceps(1,30);
        Forceps f4 = new Forceps(1,60);
        Forceps f5 = new Forceps(1,110);
        fs.add(t4);
        fs.add(t5);
        fs.add(t6);
        fs.add(f3);
        fs.add(f4);
        fs.add(f5);
        Iterator<FormicariumItem> iterFi = fs.iterator();
        int count4 = 0;
        while (iterFi.hasNext()){
            iterFi.next();
            count4++;
        }
        testEquals(count4, 2);
        System.out.println();
        // Testklasse von FormicariumSet, wenn FormicariumSet funktionieren würde
        /*
        Iterator<FormicariumItem> iterFi2 = fs.iterator();
        try{
            iterFi2.count()
        }catch (IllegalStateException ex){
            System.out.println("Successful test (exception caught): " + ex);
        }
        FormicariumItem lastElement;
        lastElement = iterFi2.next();
        testEquals(iterFi2.count(), 3);
        iterFi2.remove()
        testEquals(iterFi2.count(), 2);
        testEquals(fs.getSet().get(lastElement), 2);
        lastElement = iterFi2.next();
        testEquals(iterFi2.count(), 3);
        iterFi2.remove(2)
        testEquals(fs.getSet().get(lastElement), 1);

        try{
            iterFi2.remove(2)
        }catch (IllegalStateException ex){
            System.out.println("Successful test (exception caught): " + ex);
        }
         */


        System.out.println("Test 'quality()':");
        System.out.println("Thermometer:");
        t4.quality();
        t6.quality();
        t5.quality();
        System.out.println("Forceps:");
        f3.quality();
        f4.quality();
        f5.quality();
        System.out.println();












    }
    public static void testIdentity(Object given, Object expected) {
        if (given == expected) {
            System.out.println("Successful test");
        } else {
            System.out.println("Test NOT successful! Expected value: " + expected + " / Given value: " + given);
        }
    }

    public static void testEquals(Object given, Object expected) {
        if (given.equals(expected)) {
            System.out.println("Successful test");
        } else {
            System.out.println("Test NOT successful! Expected value: " + expected.toString() + " / Given " +
                    "value: " + given.toString());
        }
    }

    public static void testValue(double given, double expected) {
        if (given < expected + (expected + 1) / 1e12 && given > expected - (expected + 1) / 1e12) {
            System.out.println("Successful test");
        } else {
            System.out.println("Test NOT successful! Expected value: " + expected + " / Given value: " + given);
        }
    }

}
