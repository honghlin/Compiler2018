package AST;

public class SuffixOpNode extends UnaryOpNode {

    public SuffixOpNode(UnaryOp op, ExprNode expr) {

        super(op, expr);
    }

    //@Override
    //public <S,E> E accept(ASTVisitor<S,E> visitor) {

    //    return visitor.visit(this);
    //}

}
