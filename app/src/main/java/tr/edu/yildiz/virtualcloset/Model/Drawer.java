package tr.edu.yildiz.virtualcloset.Model;

public class Drawer {
    String name;
    int id, count;

    public Drawer(int id, int count, String name) {
        this.id = id;
        this.count = count;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public int getCount() {
        return count;
    }

    public String getName() {
        return name;
    }
}
