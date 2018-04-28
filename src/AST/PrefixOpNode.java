package AST;

public class PrefixOpNode extends UnaryOpNode {

    public PrefixOpNode(UnaryOp op, ExprNode expr) {

        super(op, expr);
    }

    //@Override
    //public <S,E> E accept(ASTVisitor<S,E> visitor) {

    //    return visitor.visit(this);
    //}

}
