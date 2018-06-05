package AST;

import Entity.Entity;
import FrontEnd.ASTVisitor;
import IR.Operand.Operand;

import java.util.HashMap;

public class ForNode extends StmtNode{

    private ExprNode init, cond, incr;
    private BlockNode body;

    public ForNode(Location loc, ExprNode init, ExprNode cond, ExprNode incr, StmtNode body) {

        super(loc);
        this.init = init;
        this.cond = cond;
        this.incr = incr;
        this.body = BlockNode.toBlockNode(body);
    }

    public ExprNode init() {

        return init;
    }

    public ExprNode cond() {

        return cond;
    }

    public ExprNode incr() {

        return incr;
    }

    public BlockNode body() {
        return body;
    }

    @Override
    public void accept(ASTVisitor visitor) {

        visitor.visit(this);
    }

    @Override public StmtNode copy() {

        ForNode node = new ForNode(this.location, this.init, this.cond, this.incr, this.body);
        if(init != null) node.init = init.copy();
        if(cond != null) node.cond = cond.copy();
        if(incr != null) node.incr = incr.copy();
        if(body != null) node.body = body.copy();
        return node;
    }

}
