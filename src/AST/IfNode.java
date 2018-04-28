package AST;

public class IfNode extends StmtNode {

    private ExprNode cond;
    private StmtNode thenBody, elseBody;

    public IfNode(Location loc, ExprNode c, StmtNode thenBody, StmtNode elseBody) {
        super(loc);
        this.cond = c;
        this.thenBody = BlockNode.toBlockNode(thenBody);
        this.elseBody = BlockNode.toBlockNode(elseBody);
    }

    public ExprNode cond() {

        return cond;
    }

    public StmtNode thenBody() {

        return thenBody;
    }

    public StmtNode elseBody() {

        return elseBody;
    }

    //@Override
    //public <S,E> S accept(ASTVisitor<S,E> visitor) {

    //    return visitor.visit(this);
    //}

}
