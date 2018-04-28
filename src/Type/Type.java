package Type;

abstract public class Type {

    Type(){}

    public boolean isVoid() {

        return false;
    }

    public boolean isBool() {

        return false;
    }

    public boolean isInt() {

        return false;
    }

    public boolean isString() {

        return false;
    }

    public boolean isArray() {

        return false;
    }

    public boolean isClass() {

        return false;
    }

    public boolean isFunction() {

        return false;
    }

    public boolean isNull() {

        return false;
    }

    abstract public boolean isCompatibleWith(Type other);
}
