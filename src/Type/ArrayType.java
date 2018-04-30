package Type;
import AST.Location;
import Entity.*;
import FrontEnd.LoadLibrary;
import Scope.Scope;

import java.util.LinkedList;
import java.util.List;

public class ArrayType extends Type{

    private Type baseType;

    private int dimension;

    static private Scope scope;//just for .size().

    static public void Load() {

        scope = new Scope(true);
        List<Entity> varList = new LinkedList<>();
        scope.insert(new FunctionEntity(new Location(0,0),new IntType(), "size", varList, null));
    }

    public ArrayType(Type baseType) {

        this.baseType = baseType;
    }

    public ArrayType(Type baseType, int dimension) {

        this.baseType = baseType;
        this.dimension = dimension;
    }

    public Type baseType() {

        return baseType;
    }

    static public Scope scope() {

        return scope;
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
