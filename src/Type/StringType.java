package Type;

import AST.Location;
import Entity.Entity;
import Scope.Scope;
import Entity.*;

import java.util.LinkedList;
import java.util.List;

public class StringType extends Type {

    static public StringType stringType = new StringType();

    static private Scope scope;//just for the funtion.

    static public void Load() {

        scope = new Scope(true);
        List<Entity> varList1 = new LinkedList<>();
        List<Entity> varList2 = new LinkedList<>();
        List<Entity> varList3 = new LinkedList<>();
        List<Entity> varList4 = new LinkedList<>();

        varList2.add(new Entity(new Location(0,0), new IntType(), "LFP2" + "left"));
        varList2.add(new Entity(new Location(0,0), new IntType(), "LFP2" + "right"));
        varList4.add(new Entity(new Location(0,0), new IntType(), "LFP4" + "pos"));

        Entity thisPointer = new Entity(new Location(0, 0), null, "this");
        varList1.add(0, thisPointer);
        varList2.add(0, thisPointer);
        varList3.add(0, thisPointer);
        varList4.add(0, thisPointer);

        scope.insert(new FunctionEntity(new Location(0,0),new IntType(), "length", varList1, null));
        scope.insert(new FunctionEntity(new Location(0,0),new StringType(), "substring", varList2, null));
        scope.insert(new FunctionEntity(new Location(0,0),new IntType(), "parseInt", varList3, null));
        scope.insert(new FunctionEntity(new Location(0,0),new IntType(), "ord", varList4, null));
    }

    public StringType() {}

    static public Scope scope() {

        return scope;
    }

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
