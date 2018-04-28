package AST;

import FrontEnd.ASTVisitor;

abstract public class DefinitionNode extends StmtNode {

    protected String name;

    public DefinitionNode(Location loc, String name) {

        super(loc);
        this.name = name;
    }

    public String name() {

        return name;
    }

    abstract public void accept(ASTVisitor visitor);

}
