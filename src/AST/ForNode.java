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

    @Override public StmtNode Inline(HashMap<Entity, Operand> inlineMap) {

        return this;
    }

}
