package AST;

public class WhileNode extends StmtNode {

    private ExprNode cond;
    private StmtNode body;

    public WhileNode(Location loc, ExprNode cond, StmtNode body) {

        super(loc);
        this.cond = cond;
        this.body = BlockNode.toBlockNode(body);
    }

    public ExprNode cond() {

        return cond;
    }

    public StmtNode body() {

        return body;
    }

    //@Override
    //public <S,E> S accept(ASTVisitor<S,E> visitor) {

    //    return visitor.visit(this);
    //}

}
