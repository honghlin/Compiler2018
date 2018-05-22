package IR;

import IR.Operand.Operand;

public class Binary extends Ins {

    public enum BinaryOp {
        ADD, SUB, MUL, DIV, MOD,
        LSHIFT, RSHIFT, LT, GT, LE, GE, EQ, NE,
        B_AND, B_XOR, B_OR,
        L_AND, L_OR
    }

    private Operand dest, left, right;
    BinaryOp op;

    public Binary(Operand dest, BinaryOp op,Operand left, Operand right) {
        this.dest = dest;
        this.op = op;
        this.left = left;
        this.right = right;
    }

    public Operand dest() {
        return dest;
    }

    public Operand left() {
        return left;
    }

    public Operand right() {
        return right;
    }

    public BinaryOp op() {
        return op;
    }

    @Override public String toString() {
        String t = "";
        t += (dest.toString() + " = ");
        t += (left.toString() + " " + op + " "  + right.toString() + "\n");
        //if(right != null) t += right.toString() + "\n";
        //t += "\n";
        return t;
    }

}
