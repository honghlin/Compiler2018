package AST;

import FrontEnd.ASTVisitor;

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

    @Override
    public void accept(ASTVisitor visitor) {

        visitor.visit(this);
    }

}
