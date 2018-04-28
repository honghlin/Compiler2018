package Type;

public class VoidType extends Type{

    static public VoidType voidType = new VoidType();

    public VoidType() {}

    @Override
    public boolean isVoid() {

        return true;
    }

    @Override
    public boolean isCompatibleWith(Type other) {

        return other.isVoid();
    }

    @Override
    public String toString() {

        return "void";
    }

}
