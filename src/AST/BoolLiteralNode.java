package AST;

import FrontEnd.ASTVisitor;
import Type.BoolType;
import Entity.BoolConstantEntity;
//import com.sun.org.apache.xpath.internal.operations.Bool;

public class BoolLiteralNode extends LiteralNode {

    private boolean value;
    private BoolConstantEntity entity;

    public BoolLiteralNode(Location loc, boolean value) {

        super(loc, new BoolType());
        this.value = value;
    }

    public boolean value() {

        return value;
    }

    public BoolConstantEntity entity() {

        return entity;
    }

    public void setEntity(BoolConstantEntity entity) {

        this.entity = entity;
    }

    @Override
    public void accept(ASTVisitor visitor) {

        visitor.visit(this);
    }

    @Override public String hash() {

        String t = Boolean.toString(value);
        return t;
    }

}
