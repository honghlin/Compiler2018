package AST;

import FrontEnd.ASTVisitor;
import IR.Operand.Operand;
import Entity.*;

import java.util.HashMap;

public class ReturnNode extends StmtNode {

    private ExprNode expr;

    public ReturnNode(Location loc, ExprNode expr) {

        super(loc);
        this.expr = expr;
    }

    public ReturnNode(ExprNode expr) {

        super(new Location(0 ,0));
        this.expr = expr;
    }

    public ExprNode expr() {

        return expr;
    }

    public void setExpr(ExprNode expr) {

        this.expr = expr;
    }

    @Override public void accept(ASTVisitor visitor) {

        visitor.visit(this);
    }

    @Override public StmtNode Inline(HashMap<Entity, Operand> inlineMap) {

        ReturnNode s = new ReturnNode(null);
        s.expr = expr.Inline(inlineMap);
        return s;
    }

}
