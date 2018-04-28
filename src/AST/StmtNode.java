package AST;

import FrontEnd.ASTVisitor;

abstract public class StmtNode extends Node {

    protected Location location;

    public StmtNode(Location loc) {

        this.location = loc;
    }

    @Override
    public Location location() {

        return location;
    }

    abstract public void accept(ASTVisitor visitor);

}
