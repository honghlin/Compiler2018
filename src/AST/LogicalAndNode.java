package AST;

import Entity.Entity;
import FrontEnd.ASTVisitor;
import IR.Operand.Operand;

import java.util.HashMap;

public class LogicalAndNode extends BinaryOpNode{

    public LogicalAndNode(ExprNode left, ExprNode right) {

        super(BinaryOp.L_AND, left, right);
    }

    @Override
    public void accept(ASTVisitor visitor) {

        visitor.visit(this);
    }

    @Override public ExprNode copy() {

        LogicalAndNode node = new LogicalAndNode(this.left, this.right);
        node.left = left.copy();
        node.right = right.copy();
        return node;
    }

}
