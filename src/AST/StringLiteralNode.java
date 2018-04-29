package AST;

import Entity.StringConstantEntity;
import FrontEnd.ASTVisitor;
import Type.StringType;

public class StringLiteralNode extends LiteralNode{

    private String value;
    private StringConstantEntity entity;

    public StringLiteralNode(Location loc, String value) {
        super(loc, new StringType());
        this.value = value;
    }

    public String value() {

        return value;
    }

    public StringConstantEntity entity() {

        return entity;
    }

    public void setEntity(StringConstantEntity entity) {

        this.entity = entity;
    }

    @Override
    public void accept(ASTVisitor visitor) {

        visitor.visit(this);
    }

}
