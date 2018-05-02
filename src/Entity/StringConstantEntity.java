package Entity;

import Type.Type;
import AST.ExprNode;
import AST.Location;

public class StringConstantEntity extends Entity{

    private String value;

    static public String Pre = "_StringLiteral";

    public String value() {

        return value;
    }

    public StringConstantEntity(Location loc, Type type, String name) {

        super(loc, type, name);
        this.value = name;
    }

    @Override
    public String toString() {
        return "StringConstant entity : " + name;
    }
}
