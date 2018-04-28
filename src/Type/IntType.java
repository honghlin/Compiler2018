package Type;

public class IntType extends Type {

    static public IntType intType = new IntType();

    public IntType() {}

    @Override
    public boolean isInt() {

        return true;
    }

    @Override
    public boolean isCompatibleWith(Type other) {

        return other.isInt();
    }

    @Override
    public String toString() {

        return "int";
    }
}
