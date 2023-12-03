import java.util.Iterator;

@Verantwortlich(person = "Julian Gao")
public class Formicarium {
    private final String name;
    private String species;
    private List nests;

    public Formicarium(String name) {
        this.name = name;
        this.nests = new List();
    }

    public void setSpecies(String species) {
        this.species = species;
    }
    public void removeSpecies(){
        this.species = null;
    }
    public String getSpecies() {
        return species;
    }

    public void addNest(Nest nest) {
        nests.addLast(nest);
    }
    public boolean removeNest(Nest nest) {
        return nests.remove(nest);
    }

    private Nest findNest(int id) {
        Iterator iter = nests.iterator();
        while (iter.hasNext()) {
            Nest nest = (Nest) iter.next();
            if (nest.getId() == id) {
                return nest;
            }
        }
        throw new IllegalArgumentException("Kein Nest mit der ID " + id + " gefunden.");
    }

    public void changeNestInfo(int id, double sandClayWeight) {
        Nest nest = findNest(id);
        nest.changeFilling(sandClayWeight);
    }

    public void changeNestInfo(int id, double gcHeight, double gcWidth) {
        Nest nest = findNest(id);
        nest.changeFilling(gcHeight, gcWidth);
    }

    private double calcAverageVolume(List nests) throws Exception {
        int size = nests.getSize();
        double sum = 0;
        for (Object n : nests) {
            if (n instanceof Nest) {
                sum += ((Nest) n).calcVolume();
            }
        }
        if (size == 0){
            throw new Exception("There is no Nest in this Formicarium");
        }
        return sum / size;
    }

    private List getHeatedNests() {
        List heatedNests = new List();
        for (Object n : nests) {
            if (n instanceof HeatedNest) {
                heatedNests.addLast(n);
            }
        }
        return heatedNests;
    }

    // TODO: duplizierter Code
    private List getHumidifiedNests() {
        List humidifiedNests = new List();
        for (Object n : nests) {
            if (n instanceof HumidifiedNest) {
                humidifiedNests.addLast(n);
            }
        }
        return humidifiedNests;
    }


    public double averageNestVolume() throws Exception {
        return calcAverageVolume(nests);
    }

    public double averageHeatedVolume() throws Exception {
        List heatedNests = getHeatedNests();
        return calcAverageVolume(heatedNests);
    }

    public double averageHumidifiedVolume() throws Exception {
        List humidifiedNests = getHumidifiedNests();
        return calcAverageVolume(humidifiedNests);
    }

    // TODO: sehr ähnlicher Code
    public double averageHeatedPower() throws Exception {
        List heatedNests = getHeatedNests();
        double sum = 0;
        int size = 0;
        for (Object n : heatedNests) {
            size++;
            sum += ((HeatedNest) n).getHeatingPower();
        }
        if (size == 0){
            throw new Exception("There is no Nest in this Formicarium");
        }
        return sum / size;
    }

    // TODO: duplizierter Code
    public double averageWaterVolume() throws Exception {
        List humidifiedNests = getHumidifiedNests();
        double sum = 0;
        int size = 0;
        for (Object n : humidifiedNests) {
            size++;
            sum += ((HumidifiedNest) n).getTankVolume();
        }
        if (size == 0){
            throw new Exception("There is no Nest in this Formicarium");
        }
        return sum / size;
    }

    public void averageSandClayWeight() throws Exception{
        int sizeHe = 0, sizeHu = 0;
        double sumHe = 0, sumHu = 0;
        for (Object n: nests) {
            if (n.getClass() == HeatedNest.class){
                sizeHe++;
                sumHe += ((HeatedNest) n).getSandClayWeight();
            } else if (n.getClass() == HumidifiedNest.class) {
                sizeHu++;
                sumHu += ((HumidifiedNest) n).getSandClayWeight();
            }
        }
        if ((sizeHe + sizeHu) == 0){
            throw new Exception("There is no Nest in this Formicarium");
        }
        System.out.println("Average SandClayWeight: " + (sumHe+sumHu)/(sizeHe+sizeHu));
        if (sizeHe == 0){
            System.out.println("Average SandClayWeight of HeatedNests: " + 0);
        } else {
            System.out.println("Average SandClayWeight of HeatedNests: " + sumHe/sizeHe);
        }
        if (sizeHu == 0){
            System.out.println("Average SandClayWeight of HumidifiedNests: " + 0);
        } else {
            System.out.println("Average SandClayWeight of HumidifiedNests: " + sumHu/sizeHu);
        }

    }

    public void averageGasConcreteVolume() throws Exception{
        int sizeHe = 0, sizeHu = 0;
        double sumHe = 0, sumHu = 0;
        for (Object n: nests) {
            if (n.getClass() == HeatedNest.class){
                sizeHe++;
                sumHe += ((HeatedNest) n).getGasConcreteWidth() * ((HeatedNest) n).getGasConcreteHeight();
            } else if (n.getClass() == HumidifiedNest.class) {
                sizeHu++;
                sumHu += ((HumidifiedNest) n).getGasConcreteWidth() * ((HumidifiedNest) n).getGasConcreteHeight();
            }
        }
        if ((sizeHe + sizeHu) == 0){
            throw new Exception("There is no Nest in this Formicarium");
        }
        System.out.println("Average GasConcreteVolume: " + (sumHe+sumHu)/(sizeHe+sizeHu));
        if (sizeHe == 0){
            System.out.println("Average GasConcreteVolume of HeatedNests: " + 0);
        } else {
            System.out.println("Average GasConcreteVolume of HeatedNests: " + sumHe/sizeHe);
        }
        if (sizeHu == 0){
            System.out.println("Average GasConcreteVolume of HumidifiedNests: " + 0);
        } else {
            System.out.println("Average GasConcreteVolume of HumidifiedNests: " + sumHu/sizeHu);
        }

    }

}
