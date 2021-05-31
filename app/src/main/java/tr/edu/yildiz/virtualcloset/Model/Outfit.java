package tr.edu.yildiz.virtualcloset.Model;

public class Outfit {
    int id, overhead, face, upper, lower, foot;

    public Outfit(int id, int overhead, int face, int upper, int lower, int foot) {
        this.id = id;
        this.overhead = overhead;
        this.face = face;
        this.upper = upper;
        this.lower = lower;
        this.foot = foot;
    }

    public Outfit(int overhead, int face, int upper, int lower, int foot) {
        this.overhead = overhead;
        this.face = face;
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

    public int getFace() {
        return face;
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
