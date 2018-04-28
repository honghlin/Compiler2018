package AST;

import FrontEnd.ASTVisitor;

public class PrefixOpNode extends UnaryOpNode {

    public PrefixOpNode(UnaryOp op, ExprNode expr) {

        super(op, expr);
    }

    @Override
    public void accept(ASTVisitor visitor) {

        visitor.visit(this);
    }

}
