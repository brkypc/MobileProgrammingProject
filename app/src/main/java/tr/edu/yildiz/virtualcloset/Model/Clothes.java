package tr.edu.yildiz.virtualcloset.Model;

public class Clothes {
    int id, drawerNo;
    String type, color, pattern, date, price;
    byte[] photo;

    public Clothes(int drawerNo, String type, String color, String pattern, String date, String price, byte[] photo) {
        this.drawerNo = drawerNo;
        this.type = type;
        this.color = color;
        this.pattern = pattern;
        this.date = date;
        this.price = price;
        this.photo = photo;
    }

    public Clothes(int id, int drawerNo, String type, String color, String pattern, String date, String price, byte[] photo) {
        this.id = id;
        this.drawerNo = drawerNo;
        this.type = type;
        this.color = color;
        this.pattern = pattern;
        this.date = date;
        this.price = price;
        this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public int getDrawerNo() {
        return drawerNo;
    }

    public String getType() {
        return type;
    }

    public String getColor() {
        return color;
    }

    public String getPattern() {
        return pattern;
    }

    public String getDate() {
        return date;
    }

    public String getPrice() {
        return price;
    }

    public byte[] getPhoto() {
        return photo;
    }
}
