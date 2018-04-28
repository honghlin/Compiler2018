package AST;

public class ExprStmtNode extends StmtNode {

    private ExprNode expr;

    public ExprStmtNode(Location loc, ExprNode expr) {

        super(loc);
        this.expr = expr;
    }

    public void setExpr(ExprNode expr) {

        this.expr = expr;
    }

    public ExprNode expr() {

        return expr;
    }

    //@Override
    //public <S, E> S accept(ASTVisitor<S, E> visitor) {

    //    return visitor.visit(this);
    //}

}
