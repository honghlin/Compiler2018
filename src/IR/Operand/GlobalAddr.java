package IR.Operand;

public class GlobalAddr extends Operand {

    private String name;
    private boolean IsStrConst;

    public GlobalAddr(String name, boolean IsStrConst) {

        this.name = name;
        this.IsStrConst = IsStrConst;
    }

    public String name() {
        return name;
    }

    public boolean IsStrConst() {
        return IsStrConst;
    }

    @Override public String toString() {

        return IsStrConst ? name : "qword[" + name + "]";

    }

}
