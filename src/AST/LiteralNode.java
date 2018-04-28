package AST;

import Type.Type;

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


}
