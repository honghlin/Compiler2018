package IR.Operand;

abstract public class Reg extends Operand {

    protected int index;
    protected String name;

    public int index() {

        return index;
    }

    public String name() {

        return name;
    }

    public void setIndex(int index) {

        this.index = index;
    }
}
