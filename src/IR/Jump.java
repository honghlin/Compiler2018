package IR;

import backend.IRVisitor;

public class Jump extends Ins {

    private Label Label;

    public Jump(Label Label) {
        this.Label = Label;
    }

    public Label Label() {
        return Label;
    }

    public String toString() {
        String t = "Jump " + Label.toString();
        return t;
    }
    @Override public void accept(IRVisitor visitor) {

        visitor.visit(this);
    }
}
