public class Institute {

    private final String name;

    private List list;

    public Institute(String name) {
        this.name = name;
        list = new List();
    }

    public void add(Formicarium formicarium){
        list.addLast(formicarium);
    }

    public boolean remove(Formicarium formicarium){
        return list.remove(formicarium);
    }
}
