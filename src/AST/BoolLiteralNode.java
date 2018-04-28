package AST;

import Type.BoolType;
import Entity.BoolConstantEntity;

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

    //@Override
    //public <S,E> E accept(ASTVisitor<S,E> visitor) {

    //    return visitor.visit(this);
    //}

}
