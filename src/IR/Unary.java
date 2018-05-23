package IR;

import IR.Operand.Operand;
import backend.IRVisitor;

public class Unary extends Ins {

    public enum UnaryOp {
        MINUS, L_NOT, B_NOT
    }

    private Operand Src;
    private UnaryOp op;

    public Unary(Operand Src, UnaryOp op) {
        this.Src = Src;
        this.op = op;
    }

    public Operand Src() {
        return Src;
    }

    public UnaryOp Op() {
        return op;
    }

    @Override public String toString() {
        String t = op + " " + Src.toString() + "\n";
        return t;
    }

    @Override public void accept(IRVisitor visitor) {

        visitor.visit(this);
    }
}
