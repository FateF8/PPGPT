import java.util.Iterator;

public class Test {

    /*

    Lucy:
    - Implementierung von Part, Arena und Nest
    - Schreiben der zugehörigen Zusicherungen & teilweise Test-Cases

    Julian:
    - Implementierung der Interface: Calc, Rated und Klassen: Quality, Numeric und (zum Teil) CompatibilitySet + Zusicherungen
    - MyLinkedList implementiert

    Yi:
    - Implementierung von RatedSet und StatSet, teilweise CompatibilitySet
    - Dazugehörige Zusicherungen und einige Testcases
     */
    public static void main(String[] args) {

        StatSet<Numeric,Numeric,Numeric> sSet1 = new StatSet<>();
        StatSet<Part,Part,Quality> sSet2 = new StatSet<>();
        StatSet<Arena,Part,Quality> sSet3 = new StatSet<>();
        StatSet<Nest,Part,Quality> sSet4 = new StatSet<>();
        StatSet<Part,Arena,Quality> sSet5 = new StatSet<>();
        StatSet<Arena,Arena,Quality> sSet6 = new StatSet<>();
        StatSet<Nest,Arena,Quality> sSet7 = new StatSet<>();
        StatSet<Part,Nest,Quality> sSet8 = new StatSet<>();
        StatSet<Arena,Nest,Quality> sSet9 = new StatSet<>();
        StatSet<Nest,Nest,Quality> sSet10 = new StatSet<>();
        CompatibilitySet<Numeric,Numeric> cSet1 = new CompatibilitySet<>();
        CompatibilitySet<Part,Quality> cSet2 = new CompatibilitySet<>();
        CompatibilitySet<Arena,Quality> cSet3 = new CompatibilitySet<>();
        CompatibilitySet<Nest,Quality> cSet4 = new CompatibilitySet<>();

        Numeric num1 = new Numeric(10);
        Numeric num2 = new Numeric(20);
        Numeric num3 = new Numeric(30);
        Arena a1 = new Arena(Arena.UsageArea.PROFESSIONAL, 100);
        Arena a2 = new Arena(Arena.UsageArea.HOBBY, 150);
        Arena a3 = new Arena(Arena.UsageArea.SEMIPROFESSIONAL, 170);
        Nest n1 = new Nest(Nest.UsageArea.PROFESSIONAL, 12);
        Nest n2 = new Nest(Nest.UsageArea.SEMIPROFESSIONAL, 14);
        Nest n3 = new Nest(Nest.UsageArea.HOBBY, 10);
        Part p1 = new Arena(Arena.UsageArea.PROFESSIONAL, 150);
        Part p2 = new Arena(Arena.UsageArea.SEMIPROFESSIONAL, 170);
        Part p3 = new Nest(Nest.UsageArea.PROFESSIONAL, 15);
        Part p4 = new Nest(Nest.UsageArea.HOBBY, 10);

        // Alle StatSets und CompatibilitySets befüllen
        sSet1.add(num1); sSet1.add(num2); sSet1.add(num3); sSet1.addCriterion(num3); sSet1.addCriterion(num2); sSet1.addCriterion(num1);
        sSet4.add(n1); sSet4.add(n2); sSet4.add(n3); sSet4.addCriterion(p1); sSet4.addCriterion(p3); sSet4.addCriterion(n1); sSet4.addCriterion(a1);
        sSet6.add(a1); sSet6.add(a2); sSet6.add(a3); sSet6.addCriterion(a1); sSet6.addCriterion(a2); sSet6.addCriterion(a3);
        sSet7.add(n1); sSet7.add(n2); sSet7.add(n3); sSet7.addCriterion(a1); sSet7.addCriterion(a2); sSet7.addCriterion(a3);
        sSet8.add(p2); sSet8.add(p4); sSet8.add(n1); sSet8.add(a1); sSet8.addCriterion(n1); sSet8.addCriterion(n2); sSet8.addCriterion(n3);
        sSet10.add(n1); sSet10.add(n2); sSet10.add(n3); sSet10.addCriterion(n1); sSet10.addCriterion(n2); sSet10.addCriterion(n3);

        cSet1.add(num1); cSet1.add(num2); cSet1.add(num3); cSet1.addCriterion(num1); cSet1.addCriterion(num2); cSet1.addCriterion(num3);
        cSet2.add(p1); cSet2.add(p3); cSet2.add(a1); cSet2.add(n1); cSet2.addCriterion(p2); cSet2.addCriterion(p4); cSet2.addCriterion(a3); cSet2.addCriterion(n2);
        cSet3.add(a1); cSet3.add(a2); cSet3.add(a3); cSet3.addCriterion(a1); cSet3.addCriterion(a2); cSet3.addCriterion(a3);
        cSet4.add(n1); cSet4.add(n2); cSet4.add(n3); cSet4.addCriterion(n1); cSet4.addCriterion(n2); cSet4.addCriterion(n3);

        System.out.println("Test 'Aufgabe 2':");
        //sSet5 = a vom Typ StatSet<Part,...,Quality>
        //sSet3 = b vom Typ StatSet<...,Part,Quality>
        //sSet9 = c vom Typ StatSet<Arena,Nest,Quality>

        sSet9.add(a1); sSet9.add(a2); sSet9.add(a3); sSet9.addCriterion(n1); sSet9.addCriterion(n2); sSet9.addCriterion(n3);
        Iterator<Arena> iterArena9 = sSet9.iterator();
        Iterator<Nest> iterNest9 = sSet9.criterions();
        while(iterArena9.hasNext()){
            Arena hA9 = iterArena9.next();
            System.out.println(hA9.volume());
            sSet5.add(hA9);
        }
        while(iterNest9.hasNext()){
            Nest hN9 = iterNest9.next();
            System.out.println(hN9.antSize());
            sSet3.addCriterion(hN9);
        }
        System.out.println("Test 'rated(P p)':");
        Iterator<Nest> iterNest4 = sSet4.iterator();
        Iterator<Part> iterPart4 = sSet4.criterions();
        while (iterNest4.hasNext() && iterPart4.hasNext()){
            Nest hN4 = iterNest4.next();
            Part hP4 = iterPart4.next();
            Quality hQ4 = hN4.rated(hP4);
            System.out.println(hQ4);
        }
        Iterator<Arena> iterArena6 = sSet6.iterator();
        Iterator<Arena> iterArena62 = sSet6.criterions();
        while (iterArena6.hasNext() && iterArena62.hasNext()){
            Arena hA6 = iterArena6.next();
            Arena hA62 = iterArena62.next();
            Quality hQ6 = hA6.rated(hA62);
            System.out.println(hQ6);
        }
        Iterator<Numeric> iterNum1 = sSet1.iterator();
        Iterator<Numeric> iterNum12 = sSet1.criterions();
        while( iterNum1.hasNext() && iterNum12.hasNext()){
            Numeric hNum1 = iterNum1.next();
            Numeric hNum12 = iterNum12.next();
            System.out.println(hNum1.rated(hNum12));
        }

        System.out.println();
        System.out.println("Test 'rated()':");
        Iterator<Nest> iterNest7 = sSet7.iterator();
        Iterator<Arena> iterArena7 = sSet7.criterions();
        while (iterNest7.hasNext() && iterArena7.hasNext()){
            Nest hN7 = iterNest7.next();
            Arena hA7 = iterArena7.next();
            System.out.println(hN7.rated());
            System.out.println(hA7.rated());
        }
        System.out.println();


        System.out.println("Test 'statistics()':");
        System.out.println("StatSet:");
        sSet2.statistics();
        sSet2.add(new Arena(Arena.UsageArea.PROFESSIONAL, 100));
        sSet2.addCriterion(new Arena(Arena.UsageArea.PROFESSIONAL, 100));
        sSet2.statistics();
        System.out.println("CompatibilitySet:");
        cSet1.statistics();



        testQuality();
        testNumeric();
        testArena();
        testNest();
        testStatSet();
    }


    private static void testQuality() {
        Quality hobby = Quality.HOBBY;
        Quality semiprofessional = Quality.SEMIPROFESSIONAL;
        Quality notSuitable = Quality.NOT_SUITABLE;

        System.out.println("Quality Tests:");
        System.out.println("Hobby at least Semiprofessional: " + hobby.atLeast(semiprofessional));
        System.out.println("Semiprofessional at least Hobby: " + semiprofessional.atLeast(hobby));
        System.out.println("Not Suitable at least Semiprofessional: " + notSuitable.atLeast(semiprofessional));
    }

    private static void testNumeric() {
        Numeric numeric1 = new Numeric(5);
        Numeric numeric2 = new Numeric(10);

        System.out.println("Numeric Tests:");
        System.out.println("Sum: " + numeric1.sum(numeric2).applyAsDouble(0));
        System.out.println("Ratio: " + numeric1.ratio(2).applyAsDouble(0));
    }

    private static void testArena() {
        Arena arena1 = new Arena(Arena.UsageArea.HOBBY, 5);
        Arena arena2 = new Arena(Arena.UsageArea.SEMIPROFESSIONAL, 10);

        System.out.println("Arena Tests:");
        System.out.println("Rated Arena 1 with Arena 2: " + arena1.rated(arena2).toString());
    }

    private static void testNest() {
        Nest nest1 = new Nest(Nest.UsageArea.PROFESSIONAL, 5);
        Nest nest2 = new Nest(Nest.UsageArea.SEMIPROFESSIONAL, 10);

        System.out.println("Nest Tests:");
        System.out.println("Rated Nest 1 with Nest 2: " + nest1.rated(nest2).toString());
    }

    private static void testStatSet() {
        StatSet<Arena, Part, Quality> statSet = new StatSet<>();

        Arena arena1 = new Arena(Arena.UsageArea.HOBBY, 5);
        Arena arena2 = new Arena(Arena.UsageArea.SEMIPROFESSIONAL, 10);

        statSet.add(arena1);
        statSet.add(arena2);

        System.out.println("StatSet Tests:");

        // testing Iterator
        Iterator<Arena> iterator = statSet.iterator();
        while (iterator.hasNext()) {
            Arena arena = iterator.next();
            System.out.println("Iterated Arena: " + arena);
        }
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
