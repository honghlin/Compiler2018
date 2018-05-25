package AST;

import Entity.Entity;
import IR.Operand.Operand;
import Type.Type;

import java.util.HashMap;

abstract public class LiteralNode extends ExprNode{

    protected Type type;
    protected Location location;

    public LiteralNode(Location loc, Type type) {

        super();
        this.location = loc;
        this.type = type;
    }

    @Override
    public Type type() {

        return type;
    }

    @Override
    public Location location() {

        return location;
    }

    @Override public ExprNode Inline(HashMap<Entity, Operand> inlineMap) {

        return this;
    }

}
