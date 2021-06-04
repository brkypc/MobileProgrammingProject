package tr.edu.yildiz.virtualcloset.Model;

public class Outfit {
    int id, overhead, upper, lower, foot;

    public Outfit(int id, int overhead, int upper, int lower, int foot) {
        this.id = id;
        this.overhead = overhead;
        this.upper = upper;
        this.lower = lower;
        this.foot = foot;
    }

    public Outfit(int overhead, int upper, int lower, int foot) {
        this.overhead = overhead;
        this.upper = upper;
        this.lower = lower;
        this.foot = foot;
    }

    public int getId() {
        return id;
    }

    public int getOverhead() {
        return overhead;
    }

    public int getUpper() {
        return upper;
    }

    public int getLower() {
        return lower;
    }

    public int getFoot() {
        return foot;
    }
}
