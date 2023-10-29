import codedraw.CodeDraw;

import java.sql.SQLOutput;

public class Test {

    public static void main(String[] args) {

        /*
        Arbeitsverteilung Aufgabe 1:
        Alle zusammen: erstes Konzept, Implementierung der Klassen Ant und Simulation
        Gao Julian: Grundgerüst, Implementierung der Klassen FoodSource, SingleFieldInfo, Scent und Anthill
        Xu Lucy: Ideenbeisteuerung & Implementierung Duftspur
        Yi Yan: Ideenbeisteuerung & Implementierung Bewegung (directions, boundaries) der Ameisen


        Wir haben fast immer zusammen auf Discord geredet und das Beispiel entwickelt,
        meistens hat nur eine Person aktiv programmiert, während die anderen über Bildschirm-
        übertragung zugesehen und Ideen eingebracht haben bzw. es wurde an verschiedenen
        Problemen individuell gearbeitet.

         */

        /*
        Aufgabe 2:

         */
        CodeDraw testcd = new CodeDraw(100, 100);

        System.out.println("Test 'FoodSources': ");
        FoodSource fS = new FoodSource(10, 10, testcd);
        int initialAmount = fS.getFoodAmount();
        fS.decreaseFood();
        if (fS.getFoodAmount() == initialAmount - 1) {
            System.out.println("Successful test: Food source depleted");
        } else {
            System.out.println("Test NOT successful! Expected food amount: " + (initialAmount - 1) + ", Given amount: " + fS.getFoodAmount());
        }

        System.out.println("Test 'Wind':");
        Wind w = new Wind(0, 50, 10, 10, 3, 0);
        


        Simulation simulation = new Simulation();
        while(true) {
            simulation.draw();
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
