package AST;

import Entity.Entity;
import FrontEnd.ASTVisitor;
import IR.Operand.Operand;

import java.util.HashMap;

public class BreakNode extends StmtNode {

    public BreakNode(Location loc) {

        super(loc);
    }

    @Override public void accept(ASTVisitor visitor) {

        visitor.visit(this);
    }

    @Override public StmtNode copy() {

        return this;
    }


}
