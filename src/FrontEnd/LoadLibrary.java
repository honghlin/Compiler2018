package FrontEnd;

import Entity.*;
import AST.Location;
import Type.*;

import java.util.LinkedList;
import java.util.List;

public class LoadLibrary {

    static public List<FunctionEntity> LibFunction = new LinkedList<>();
    //private static String LFP = "LF";

    LoadLibrary() {
        List<Entity> varList1 = new LinkedList<>();
        List<Entity> varList2 = new LinkedList<>();
        List<Entity> varList3 = new LinkedList<>();
        List<Entity> varList4 = new LinkedList<>();
        List<Entity> varList5 = new LinkedList<>();
        List<Entity> varList6 = new LinkedList<>();
        List<Entity> varList7 = new LinkedList<>();
        List<Entity> varList8 = new LinkedList<>();
        List<Entity> varList9 = new LinkedList<>();
        List<Entity> varList10 = new LinkedList<>();

        varList1.add(new Entity(new Location(0,0), new StringType(), "LFP1" + "str"));
        varList2.add(new Entity(new Location(0,0), new StringType(), "LFP2" + "str"));
        varList5.add(new Entity(new Location(0,0), new IntType(), "LFP5" + "i"));
        varList7.add(new Entity(new Location(0,0), new IntType(), "LFP7" + "left"));
        varList7.add(new Entity(new Location(0,0), new IntType(), "LFP8" + "right"));
        varList9.add(new Entity(new Location(0,0), new IntType(), "LFP10" + "pos"));


        LibFunction.add(new FunctionEntity(new Location(0,0),new VoidType(), "print", varList1, null));
        LibFunction.add(new FunctionEntity(new Location(0,0),new VoidType(), "println", varList2, null));
        LibFunction.add(new FunctionEntity(new Location(0,0),new StringType(), "getString", varList3, null));
        LibFunction.add(new FunctionEntity(new Location(0,0),new IntType(), "getInt", varList4, null));
        LibFunction.add(new FunctionEntity(new Location(0,0),new StringType(), "toString", varList5, null));
        LibFunction.add(new FunctionEntity(new Location(0,0),new IntType(), "length", varList6, null));
        LibFunction.add(new FunctionEntity(new Location(0,0),new StringType(), "subString", varList7, null));
        LibFunction.add(new FunctionEntity(new Location(0,0),new IntType(), "parseInt", varList8, null));
        LibFunction.add(new FunctionEntity(new Location(0,0),new IntType(), "ord", varList9, null));
        LibFunction.add(new FunctionEntity(new Location(0,0),new IntType(), "size", varList10, null));

    }

    public static List<FunctionEntity>  LibFunc() {

        return LibFunction;
    }

}
