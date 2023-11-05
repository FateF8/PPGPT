import codedraw.CodeDraw;

import java.util.ArrayList;
import java.util.List;

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
        Jedes Gruppenmitglied hat sich 2 Beispiele von der Angabe ausgesucht und ein Konzept erfasst.
        Dieses haben wir anschließend versucht zu realisieren.

        Julian:
        -Erschöpfen und Entstehen von Futterquellen hinzugefügt
        -Regen und Wind hinzugefügt. Der Regen schwächt alle Duftspuren ab und die Ameisen flüchten sofort zum Bau zurück, da sie unter diesen Bedingungen keine Straßen bilden können.
        Der Wind kann je nach Stärke die Futterquellen und Hindernisse verschieden weit verschieben.
        -Verbesserung Aufgabe 1: Hashmap um switch cases in neighborsOffset zu vermeiden, graphische Ausgabe hochskaliert, Verbesserung vom foraging und explore Zustand

        Yi:
        -Tag-Nacht-Zyklus hinzugefügt, wobei die Ameisen ab 22Uhr, je nach Entfernung anfangen zurück zum Bau zu gehen.
        -Alter hinzugefügt, wobei die älteren Ameisen nahezu perfekt zum Bau finden und die jüngeren einen recht schlechten Weg gehen.
        -Diverse kleine Bugfixes.
        -Verbesserung Aufgabe 1: Interface implementiert und FoodSources mit Anthill somit kombiniert

        Lucy:
        - Hinzufügen von Hindernissen mit verschiedenen Schwierigkeitsgrade; Ameise wird beim Überwinden eines
          größeren Hindernisses stärker, wenn Ameise zu schwach ist, um das Hindernis zu überwinden, umgeht sie es
        - Individuelle Duftspur hinzugefügt, jede Ameise hat ein eigenes Gedächtnis, von wo sie gekommen ist und findet
          somit schneller zurück zum Bau
        - Verbesserung Aufgabe 1: antsMove in Ants ausgelagert
         */


        CodeDraw testcd = new CodeDraw(100, 100);

        List<SpecialPoint> specialPoints = new ArrayList<>();

        FoodSource fS = new FoodSource(10, 10, testcd);
        Ant a = new Ant(10, 10, 0);
        int initialAmount = fS.getFoodAmount();
        double decayFactor = 0.98;

        System.out.println("Test 'Food Source Limitation': ");
        a.takeFood(fS);
        testValue(fS.getFoodAmount(), initialAmount - 1);

        // Bitte auskommentieren, wenn nicht getestet wird
        /*System.out.println("Test 'Rain':");
        testIdentity(Rain.isRaining(), false);
        Rain.setRainSpawnTimer(2);
        Rain.setRainDespawnTimer(1);
        Rain.rainTimer();
        testIdentity(Rain.isRaining(), false);
        Rain.rainTimer();
        testIdentity(Rain.isRaining(), true);*/

        System.out.println("Test 'Wind moving Food Source':");
        Wind w = new Wind(10, 10, 3, 3, 2, 45);
        fS.move(w.getdx() * w.getStrength(), w.getdy() * w.getStrength());
        testValue(fS.getX(), 16); // + 2 * 3
        testValue(fS.getY(), 4); // - 2 * 3

        System.out.println("Test 'Interface':");
        List<SpecialPoint> s = new ArrayList<>();
        s.add(new Anthill(5,5, 15));
        s.add(new FoodSource(2,2,testcd));
        s.add(new Obstacle(3,3,testcd));
        testValue(s.size(),3);
        testEquals(s.get(2).getClass(), Obstacle.class);

        System.out.println("Test 'Obstacles':");
        Obstacle obstacle = new Obstacle(9,9,testcd);
        obstacle.setLevel(10);
        specialPoints.add(obstacle);
        a.antsMove(new Scent(decayFactor), new Scent(decayFactor),specialPoints);
        testValue(a.getX(),10);
        testValue(a.getY(),10);
        obstacle.setLevel(0);
        specialPoints.add(obstacle);
        a.antsMove(new Scent(decayFactor), new Scent(decayFactor),specialPoints);
        assert(a.getX() != 10 || a.getY() != 10);


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
