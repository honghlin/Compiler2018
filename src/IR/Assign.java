package IR;

import IR.Operand.Operand;
import backend.IRVisitor;

public class Assign extends Ins{

    private Operand lhs, rhs;

    public Assign(Operand lhs, Operand rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public Operand lhs() {
        return lhs;
    }

    public Operand rhs() {
        return rhs;
    }

    @Override public String toString() {

        String t = "Assign ";
  //    if(lhs == null) {
  //        int a = 1 + 1;
  //    }
        t += (lhs.toString() + " ");
        t += (rhs.toString() + "\n");
        return t;
    }

    @Override public void accept(IRVisitor visitor) {

        visitor.visit(this);
    }

}
