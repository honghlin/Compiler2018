package AST;

import FrontEnd.ASTVisitor;

public class BreakNode extends StmtNode {

    public BreakNode(Location loc) {

        super(loc);
    }

    @Override
    public void accept(ASTVisitor visitor) {

        visitor.visit(this);
    }

}
