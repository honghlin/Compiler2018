package IR;

public class Label extends Ins {

    private String name;
    private static int n = 0;

    public Label() {
        name = "_Label" + (n++);
    }

    public Label(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    @Override public String toString() {
        String t = name + ":\n";
        return t;
    }

}
