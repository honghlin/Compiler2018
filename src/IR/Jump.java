package IR;

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
}
