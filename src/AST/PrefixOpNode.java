package AST;

import Entity.Entity;
import FrontEnd.ASTVisitor;
import IR.Operand.Operand;

import java.util.HashMap;

public class PrefixOpNode extends UnaryOpNode {

    public PrefixOpNode(UnaryOp op, ExprNode expr) {

        super(op, expr);
    }

    @Override public void accept(ASTVisitor visitor) {

        visitor.visit(this);
    }

    @Override public ExprNode copy() {

        PrefixOpNode node = new PrefixOpNode(this.operator, this.expr);
        node.expr = expr.copy();
        node.type = type;
        return node;
    }

}
