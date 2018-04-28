package AST;

import FrontEnd.ASTVisitor;

public class LogicalAndNode extends BinaryOpNode{

    public LogicalAndNode(ExprNode left, ExprNode right) {

        super(BinaryOp.L_AND, left, right);
    }

    @Override
    public void accept(ASTVisitor visitor) {

        visitor.visit(this);
    }

}
