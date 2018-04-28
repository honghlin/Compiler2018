package AST;

import FrontEnd.ASTVisitor;

public class LogicalOrNode extends BinaryOpNode {

    public LogicalOrNode(ExprNode left, ExprNode right) {

        super(BinaryOp.L_OR, left, right);
    }

    @Override
    public void accept(ASTVisitor visitor) {

        visitor.visit(this);
    }

}
