package AST;

public class LogicalAndNode extends BinaryOpNode{

    public LogicalAndNode(ExprNode left, ExprNode right) {

        super(BinaryOp.L_AND, left, right);
    }

    //@Override
    //public <S,E> E accept(ASTVisitor<S,E> visitor) {
    //    return visitor.visit(this);
    //}

}
