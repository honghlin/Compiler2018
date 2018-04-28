package AST;

import FrontEnd.ASTVisitor;

public class ReturnNode extends StmtNode {

    private ExprNode expr;

    public ReturnNode(Location loc, ExprNode expr) {

        super(loc);
        this.expr = expr;
    }

    public ExprNode expr() {

        return expr;
    }

    public void setExpr(ExprNode expr) {

        this.expr = expr;
    }

    @Override
    public void accept(ASTVisitor visitor) {

        visitor.visit(this);
    }

}
