package AST;

import Entity.Entity;
import FrontEnd.ASTVisitor;
import IR.Operand.Operand;

import java.util.HashMap;

public class WhileNode extends StmtNode {

    private ExprNode cond;
    private StmtNode body;

    public WhileNode(Location loc, ExprNode cond, StmtNode body) {

        super(loc);
        this.cond = cond;
        this.body = BlockNode.toBlockNode(body);
    }

    public ExprNode cond() {

        return cond;
    }

    public StmtNode body() {

        return body;
    }

    @Override
    public void accept(ASTVisitor visitor) {

        visitor.visit(this);
    }

    @Override public StmtNode Inline(HashMap<Entity, Operand> inlineMap) {

        return this;
    }

    @Override public WhileNode copy() {

        WhileNode node = new WhileNode(this.location, this.cond, this.body);
        if(cond != null) node.cond = cond.copy();
        if(body != null) node.body = body.copy();
        return node;
    }

}
