package Type;

public class NullType extends ClassType {

    static public NullType nullType = new NullType();

    public NullType() {

        super("null");
    }

    @Override
    public boolean isNull() {

        return true;
    }

    @Override
    public boolean isCompatibleWith(Type other) {

        return other.isNull() || other.isClass() || other.isArray();
    }

}
