package AST;

import FrontEnd.ASTVisitor;
import Type.Type;

public class UnaryOpNode extends ExprNode {

    public enum UnaryOp {
        PRE_INC, PRE_DEC, SUF_INC, SUF_DEC,
        ADD, MINUS, L_NOT, B_NOT
    }

    private UnaryOp operator;
    private Type type;
    private ExprNode expr;

    public UnaryOpNode(UnaryOp op, ExprNode expr) {

        this.operator = op;
        this.expr = expr;
    }

    public UnaryOp operator() {

        return operator;
    }

    @Override
    public Type type() {

        return expr.type();
    }

    public ExprNode expr() {

        return expr;
    }

    public void setExpr(ExprNode expr) {

        this.expr = expr;
    }

    @Override
    public Location location() {

        return expr.location();
    }

    @Override
    public void accept(ASTVisitor visitor) {

        visitor.visit(this);
    }

}
