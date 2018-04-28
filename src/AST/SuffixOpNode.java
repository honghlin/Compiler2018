package AST;

import FrontEnd.ASTVisitor;

public class SuffixOpNode extends UnaryOpNode {

    public SuffixOpNode(UnaryOp op, ExprNode expr) {

        super(op, expr);
    }

    @Override
    public void accept(ASTVisitor visitor) {

        visitor.visit(this);
    }

}
