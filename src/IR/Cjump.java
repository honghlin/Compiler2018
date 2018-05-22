package IR;

//import IR.Ins;

import IR.Operand.Operand;



public class Cjump extends Ins {

    public enum BinaryOp {
        ADD, SUB, MUL, DIV, MOD,
        LSHIFT, RSHIFT, LT, GT, LE, GE, EQ, NE,
        B_AND, B_XOR, B_OR,
        L_AND, L_OR
    }

    private Operand left, right;
    private BinaryOp op;
    private Label TrueLabel;

    public Cjump(Operand left, Operand right, BinaryOp op, Label TrueLabel) {

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

    public BinaryOp Op() {
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

}
