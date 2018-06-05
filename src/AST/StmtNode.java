package AST;

import Entity.Entity;
import FrontEnd.ASTVisitor;
import IR.Operand.Operand;

import java.util.HashMap;

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

    abstract public StmtNode Inline(HashMap<Entity, Operand> inlineMap);

    abstract public StmtNode copy();

}
