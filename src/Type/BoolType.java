package Type;

public class BoolType extends Type {

    static public BoolType boolType = new BoolType();

    public BoolType() {}

    @Override
    public boolean isBool(){

        return true;
    }

    @Override
    public boolean isCompatibleWith(Type other) {

        return other.isBool();
    }

    @Override
    public String toString() {

        return "bool";
    }

}
