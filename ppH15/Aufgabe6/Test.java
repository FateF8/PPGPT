public class Test {

    public static void main(String[] args) throws Exception {
        Institute i = new Institute("TestInstitute");
        Formicarium f = new Formicarium("TestFormicarium");
        Nest hu1 = new HumidifiedNest(3,3,1,10);
        Nest hu2 = new HumidifiedNest(3,3,1,40);
        Nest he1 = new HeatedNest(3,3,2,10);
        Nest he2 = new HeatedNest(3,3,3,15);
        hu1.changeFilling(10);
        hu2.changeFilling(20);
        he1.changeFilling(15);
        he2.changeFilling(35);
        f.addNest(hu1);
        System.out.println(f.removeNest(hu1));
        f.addNest(hu1);
        i.add(f);
        System.out.println(i.remove(f));
        System.out.println(i.remove(f));

        try{
            f.averageHeatedPower();
        }catch (Exception ex){
            System.out.println("Successful test (exception caught): " + ex);
        }
        f.addNest(hu1);
        f.addNest(hu2);
        f.addNest(he1);
        f.addNest(he2);
        System.out.println(f.averageHeatedPower());
        f.averageSandClayWeight();
        hu1.changeFilling(5,3);
        hu2.changeFilling(2,7);
        he1.changeFilling(10,2);
        he2.changeFilling(4,6);
        f.averageGasConcreteVolume();
        f.averageSandClayWeight();
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
