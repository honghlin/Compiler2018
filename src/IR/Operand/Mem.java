package IR.Operand;

public class Mem extends Address {

    private Reg base;
    private Reg index;
    private int s;
    private int d;

    public Mem(Reg base, Reg index, int s, int d) {
        this.base = base;
        this.index = index;
        this.s = s;
        this.d = d;
    }

    public Reg base() {
        return base;
    }

    public Reg index() {
        return index;
    }

    public int s() {
        return s;
    }

    public int d() {
        return d;
    }

    @Override public String toString() {

        String t = "qword[";
        if(base != null) t += base.toString();
        if(index != null) t += " + " + index.toString() + " * " + Integer.toString(s);
        if(d != 0) {
            if(d < 0) {
                t += " - " + Integer.toString(-d);
            } else {
                t += " + " + Integer.toString(d);
            }
        }
        t += "]";
        return t;
    }

}
