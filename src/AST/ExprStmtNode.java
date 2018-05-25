package AST;

import Entity.Entity;
import FrontEnd.ASTVisitor;
import IR.Operand.Operand;

import java.util.HashMap;

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

    @Override public StmtNode Inline(HashMap<Entity, Operand> inlineMap) {
        ExprStmtNode node = new ExprStmtNode(this.location, this.expr);
        node.expr = expr.Inline(inlineMap);
        return node;
    }

}
