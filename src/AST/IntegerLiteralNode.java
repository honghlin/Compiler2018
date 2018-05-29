package AST;

import FrontEnd.ASTVisitor;
import Type.IntType;

public class IntegerLiteralNode extends LiteralNode{

    private long value;

    public IntegerLiteralNode(Location loc, long value) {
        super(loc, new IntType());
        this.value = value;
    }

    @Override
    public void accept(ASTVisitor visitor) {

        visitor.visit(this);
    }

    public long value() {

        return value;
    }

    @Override public String hash() {

        String t = Long.toString(value);
        return t;
    }

}
