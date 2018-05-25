package AST;

import Entity.Entity;
import FrontEnd.ASTVisitor;
import IR.Operand.Operand;
import Type.Type;

import java.util.HashMap;

public class BinaryOpNode extends ExprNode{


    public enum BinaryOp {
        ADD, SUB, MUL, DIV, MOD,
        LSHIFT, RSHIFT, LT, GT, LE, GE, EQ, NE,
        B_AND, B_XOR, B_OR,
        L_AND, L_OR
    }

    protected Type type;//private
    protected BinaryOp operator;
    protected ExprNode left, right;

    public BinaryOpNode(BinaryOp op, ExprNode left, ExprNode right) {

        super();
        this.operator = op;
        this.left = left;
        this.right = right;
    }

    public BinaryOpNode(Type t, BinaryOp op, ExprNode left, ExprNode right) {
        super();
        this.type = t;
        this.operator = op;
        this.left = left;
        this.right = right;
    }

    public BinaryOp operator() {

        return operator;
    }

    public void setOperator(BinaryOp operator) {

        this.operator = operator;
    }

    public ExprNode left() {

        return left;
    }

    public void setLeft(ExprNode left) {

        this.left = left;
    }

    public ExprNode right() {

        return right;
    }

    public void setRight(ExprNode right) {

        this.right = right;
    }

    public void setType(Type type) {

        this.type = type;
    }

    @Override
    public Type type() {

        return (type != null) ? type : left.type();
    }

    @Override
    public Location location() {

        return left.location();
    }

    @Override
    public void accept(ASTVisitor visitor) {

        visitor.visit(this);
    }

    @Override public ExprNode Inline(HashMap<Entity, Operand> inlineMap) {

        BinaryOpNode node = new BinaryOpNode(this.operator, this.left, this.right);
        node.left = left.Inline(inlineMap);
        node.right = right.Inline(inlineMap);
        return node;
    }

}
