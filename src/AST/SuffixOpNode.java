package AST;

import Entity.Entity;
import FrontEnd.ASTVisitor;
import IR.Operand.Operand;

import java.util.HashMap;

public class SuffixOpNode extends UnaryOpNode {

    public SuffixOpNode(UnaryOp op, ExprNode expr) {

        super(op, expr);
    }

    @Override public void accept(ASTVisitor visitor) {

        visitor.visit(this);
    }

    @Override public ExprNode Inline(HashMap<Entity, Operand> inlineMap) {

        SuffixOpNode node = new SuffixOpNode(this.operator, this.expr);
        node.expr = expr.Inline(inlineMap);
        return node;
    }

    @Override public ExprNode copy() {

        SuffixOpNode node = new SuffixOpNode(this.operator, this.expr);
        node.expr = expr.copy();
        node.type = type;
        return node;
    }

}
