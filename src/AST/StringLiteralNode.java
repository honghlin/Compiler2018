package AST;

import Entity.StringConstantEntity;
import Type.StringType;

public class StringLiteralNode extends LiteralNode{

    private String value;
    private StringConstantEntity entity;

    public StringLiteralNode(Location loc, String value) {
        super(loc, new StringType());
        this.value = value;
    }

    public StringConstantEntity entity() {

        return entity;
    }

    public void setEntity(StringConstantEntity entity) {

        this.entity = entity;
    }

    //@Override
    //public <S,E> E accept(ASTVisitor<S,E> visitor) {

    //    return visitor.visit(this);
    //}

}
