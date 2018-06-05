package AST;

import Entity.Entity;
import FrontEnd.ASTVisitor;
import IR.Operand.Operand;

import java.util.HashMap;

public class LogicalOrNode extends BinaryOpNode {

    public LogicalOrNode(ExprNode left, ExprNode right) {

        super(BinaryOp.L_OR, left, right);
    }

    @Override
    public void accept(ASTVisitor visitor) {

        visitor.visit(this);
    }

    @Override public ExprNode copy() {

        LogicalOrNode node = new LogicalOrNode(this.left, this.right);
        node.left = left.copy();
        node.right = right.copy();
        return node;
    }

}
