package tr.edu.yildiz.virtualcloset.Model;

public class Event {
    int id, outfitNo;
    String name, type, date, location;

    public Event(int id, int outfitNo, String name, String type, String date, String location) {
        this.id = id;
        this.outfitNo = outfitNo;
        this.name = name;
        this.type = type;
        this.date = date;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public int getOutfitNo() {
        return outfitNo;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }
}
