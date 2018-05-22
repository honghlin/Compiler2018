package IR.Operand;

public class Imm extends Operand{

    private long value;

    public Imm(long value) {

        this.value = value;
    }

    public long value() {

        return value;
    }

    @Override public String toString() {

        return Long.toString(value);
    }

}
