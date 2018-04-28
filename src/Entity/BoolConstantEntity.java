package Entity;

import AST.Location;
import Type.Type;

public class BoolConstantEntity extends Entity {

    private boolean value;

    public boolean Value() {
        return value;
    }

    public BoolConstantEntity(Location loc, Type type, String name, boolean value) {

        super(loc, type, name);
        this.value = value;
    }

    @Override
    public String toString() {
        return "StringConstant entity : " + name;
    }

}
