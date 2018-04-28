package AST;

public class ForNode extends StmtNode{

    private ExprNode init, cond, incr;
    private BlockNode body;

    public ForNode(Location loc, ExprNode init, ExprNode cond, ExprNode incr, StmtNode body) {

        super(loc);
        this.init = init;
        this.cond = cond;
        this.incr = incr;
        this.body = BlockNode.toBlockNode(body);
    }

    public ExprNode init() {

        return init;
    }

    public ExprNode cond() {

        return cond;
    }

    public ExprNode incr() {

        return incr;
    }

    public BlockNode body() {
        return body;
    }

    //@Override
    //public <S,E> S accept(ASTVisitor<S,E> visitor) {

    //    return visitor.visit(this);
    //}

}
