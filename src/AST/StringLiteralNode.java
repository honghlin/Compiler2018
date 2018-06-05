package AST;

import Entity.Entity;
import Entity.StringConstantEntity;
import FrontEnd.ASTVisitor;
import IR.Operand.Operand;
import Type.StringType;

import java.util.HashMap;

public class StringLiteralNode extends LiteralNode{

    private String value;
    private StringConstantEntity entity;
    //int r = 0;

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

    @Override public ExprNode Inline(HashMap<Entity, Operand> inlineMap) {

        return this;
    }

    @Override public ExprNode copy() {

        return this;
    }

    @Override public String hash() {

        String t = value;
        //t += Integer.toString(++r);
        //t += Integer.toString(++r);
        //t += Integer.toString(++r);
        return t;
    }

}
