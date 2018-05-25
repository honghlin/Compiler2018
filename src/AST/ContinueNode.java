package AST;

import Entity.Entity;
import FrontEnd.ASTVisitor;
import IR.Operand.Operand;

import java.util.HashMap;

public class ContinueNode extends StmtNode {

    public ContinueNode(Location loc) {

        super(loc);
    }

    @Override public void accept(ASTVisitor visitor) {

       visitor.visit(this);
   }

    @Override public StmtNode Inline(HashMap<Entity, Operand> inlineMap) {

        return this;
    }

}
