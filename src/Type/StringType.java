package Type;

public class StringType extends Type {

    static public StringType stringType = new StringType();

    public StringType() {}

    @Override
    public boolean isString() {

        return true;
    }

    @Override
    public boolean isCompatibleWith(Type other) {

        return other.isString();
    }

    @Override
    public String toString() {

        return "string";
    }

}
