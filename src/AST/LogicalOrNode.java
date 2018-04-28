package AST;

public class LogicalOrNode extends BinaryOpNode {

    public LogicalOrNode(ExprNode left, ExprNode right) {

        super(BinaryOp.L_OR, left, right);
    }

    //@Override
    //public <S,E> E accept(ASTVisitor<S,E> visitor) {

    //    return visitor.visit(this);
    //}

}
