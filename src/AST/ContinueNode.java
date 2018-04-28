package AST;

import FrontEnd.ASTVisitor;

public class ContinueNode extends StmtNode {

    public ContinueNode(Location loc) {

        super(loc);
    }

   @Override
   public void accept(ASTVisitor visitor) {

       visitor.visit(this);
   }

}
