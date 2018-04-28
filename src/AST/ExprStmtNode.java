package AST;

import FrontEnd.ASTVisitor;

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

    @Override
    public void accept(ASTVisitor visitor) {

        visitor.visit(this);
    }

}
