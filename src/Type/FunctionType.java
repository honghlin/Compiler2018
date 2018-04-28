package Type;

import Entity.FunctionEntity;

public class FunctionType extends Type{

    private String name;

    private FunctionEntity entity;

    public FunctionType(String name) {

        this.name = name;
    }

    public String name() {

        return name;
    }

    public FunctionEntity entity() {

        return entity;
    }

    public void setEntity(FunctionEntity entity) {

        this.entity = entity;
    }

    @Override
    public boolean isFunction() {

        return true;
    }

    @Override
    public boolean isCompatibleWith(Type other) {
        if (!other.isFunction()) return false;
        return entity.equals(((FunctionType)other).entity);
    }

    @Override
    public String toString() {
        return "function";
    }
}
