package IR;

import backend.IRVisitor;

import java.util.ArrayList;
import java.util.List;

public class Label extends Ins {

    private String name;

    private static int n = 0;

    public List<Ins> prev = new ArrayList<>();

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
        String t = name + "\n";
        return t;
    }

    @Override public void accept(IRVisitor visitor) {

        visitor.visit(this);
    }
}
