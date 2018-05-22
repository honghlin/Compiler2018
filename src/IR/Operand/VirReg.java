package IR.Operand;

public class VirReg extends Reg{

    public VirReg(int n) {
        this.name = "Reg" + Integer.toString(n);
        this.index = n;
    }

    @Override public String toString() {

        return "$" + Integer.toString(index);
    }

}
