package AST;

import Entity.Entity;
import FrontEnd.ASTVisitor;
import IR.Operand.Operand;

import java.util.HashMap;

public class IfNode extends StmtNode {

    private ExprNode cond;
    private StmtNode thenBody, elseBody;

    public IfNode(Location loc, ExprNode c, StmtNode thenBody, StmtNode elseBody) {
        super(loc);
        this.cond = c;
        this.thenBody = BlockNode.toBlockNode(thenBody);
        this.elseBody = BlockNode.toBlockNode(elseBody);
    }

    public ExprNode cond() {

        return cond;
    }

    public StmtNode thenBody() {

        return thenBody;
    }

    public StmtNode elseBody() {

        return elseBody;
    }

    @Override public void accept(ASTVisitor visitor) {

        visitor.visit(this);
    }

    @Override public IfNode copy() {

        IfNode node = new IfNode(this.location, this.cond, this.thenBody, this.elseBody);
        node.cond = cond.copy();
        node.thenBody = thenBody.copy();
        if(elseBody != null) node.elseBody = elseBody.copy();
        return node;
    }

}
