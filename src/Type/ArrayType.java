package Type;

public class ArrayType extends Type{

    private Type baseType;

    private int dimension;

    public ArrayType(Type baseType, int dimension) {

        this.baseType = baseType;
        this.dimension = dimension;
    }

    public Type baseType() {

        return baseType;
    }

    @Override
    public boolean isCompatibleWith(Type other) {

        if (other.isNull())    return true;
        if (!other.isArray() ) return false;
        return baseType.isCompatibleWith(((ArrayType)other).baseType);
    }

    @Override
    public boolean isArray() {

        return true;
    }

    @Override
    public String toString() {

        return baseType.toString() + "dimension(" + dimension + ")";
    }

    public Type deepType() {

        if (dimension == 1) {
            return baseType;
        }
        else {
            return new ArrayType(baseType, dimension - 1);
        }
    }

}
