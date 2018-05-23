package IR;

//import IR.Ins;

import IR.Operand.Operand;
import backend.IRVisitor;


public class Cjump extends Ins {

    public enum Type {
        EQ, NE, GT, GE, LT, LE, BOOL
    }

    private Operand left, right;
    private Type op;
    private Label TrueLabel;

    public Cjump(Operand left, Operand right, Type op, Label TrueLabel) {

        this.left = left;
        this.right = right;
        this.op = op;
        this.TrueLabel = TrueLabel;

    }

    public Operand left() {
        return left;
    }

    public Operand right() {
        return right;
    }

    public Type Op() {
        return op;
    }

    public Label TrueLabel() {
        return TrueLabel;
    }

    @Override public String toString() {
        String t = "Cjump ";
        t += (left.toString() + " " + op + " " + right.toString() + " ");
        t += TrueLabel.toString();
        return t;
    }

    @Override public void accept(IRVisitor visitor) {

        visitor.visit(this);
    }
}
