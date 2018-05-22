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
        // List<Entity> varList6 = new LinkedList<>();
        //List<Entity> varList7 = new LinkedList<>();
        //List<Entity> varList8 = new LinkedList<>();
        //List<Entity> varList9 = new LinkedList<>();
        //List<Entity> varList10 = new LinkedList<>();

        varList1.add(new Entity(new Location(0,0), new StringType(), "LFP1" + "str"));
        varList2.add(new Entity(new Location(0,0), new StringType(), "LFP2" + "str"));
        varList5.add(new Entity(new Location(0,0), new IntType(), "LFP5" + "i"));
        // varList7.add(new Entity(new Location(0,0), new IntType(), "LFP7" + "left"));
        // varList7.add(new Entity(new Location(0,0), new IntType(), "LFP8" + "right"));
        // varList9.add(new Entity(new Location(0,0), new IntType(), "LFP10" + "pos"));

        LibFunction.add(new FunctionEntity(new Location(0,0),new VoidType(), "print", varList1, null));
        LibFunction.add(new FunctionEntity(new Location(0,0),new VoidType(), "println", varList2, null));
        LibFunction.add(new FunctionEntity(new Location(0,0),new StringType(), "getString", varList3, null));
        LibFunction.add(new FunctionEntity(new Location(0,0),new IntType(), "getInt", varList4, null));
        LibFunction.add(new FunctionEntity(new Location(0,0),new StringType(), "toString", varList5, null));
       // LibFunction.add(new FunctionEntity(new Location(0,0),new IntType(), "length", varList6, null));
       // LibFunction.add(new FunctionEntity(new Location(0,0),new StringType(), "subString", varList7, null));
       // LibFunction.add(new FunctionEntity(new Location(0,0),new IntType(), "parseInt", varList8, null));
       // LibFunction.add(new FunctionEntity(new Location(0,0),new IntType(), "ord", varList9, null));
       // LibFunction.add(new FunctionEntity(new Location(0,0),new IntType(), "size", varList10, null));

        ArrayType At = new ArrayType(new NullType());
        StringType St = new StringType();
        At.Load();
        St.Load();

        List<Entity> varList = new LinkedList<>();
        varList.add(new Entity(new Location(0,0), new IntType(), "malloc" + "i"));
        FunctionEntity malloc = new FunctionEntity(new Location(0,0),new IntType(), "malloc", varList, null);
        LibFunction.add(malloc);

        List<Entity> varList_add = new LinkedList<>();
        varList_add.add(new Entity(new Location(0,0), new StringType(), "str_i"));
        varList_add.add(new Entity(new Location(0,0), new StringType(), "str_j"));
        FunctionEntity Str_ADD = new FunctionEntity(new Location(0,0),new IntType(), "Str_ADD", varList_add, null);
        LibFunction.add(Str_ADD);

        List<Entity> varList_eq = new LinkedList<>();
        varList_add.add(new Entity(new Location(0,0), new StringType(), "str_i"));
        varList_add.add(new Entity(new Location(0,0), new StringType(), "str_j"));
        FunctionEntity Str_EQ = new FunctionEntity(new Location(0,0),new IntType(), "Str_EQ", varList_eq, null);
        LibFunction.add(Str_EQ);

        List<Entity> varList_ne = new LinkedList<>();
        varList_add.add(new Entity(new Location(0,0), new StringType(), "str_i"));
        varList_add.add(new Entity(new Location(0,0), new StringType(), "str_j"));
        FunctionEntity Str_NE = new FunctionEntity(new Location(0,0),new IntType(), "Str_NE", varList_ne, null);
        LibFunction.add(Str_NE);

        List<Entity> varList_lt = new LinkedList<>();
        varList_add.add(new Entity(new Location(0,0), new StringType(), "str_i"));
        varList_add.add(new Entity(new Location(0,0), new StringType(), "str_j"));
        FunctionEntity Str_LT = new FunctionEntity(new Location(0,0),new IntType(), "Str_LT", varList_lt, null);
        LibFunction.add(Str_LT);

        List<Entity> varList_gt = new LinkedList<>();
        varList_add.add(new Entity(new Location(0,0), new StringType(), "str_i"));
        varList_add.add(new Entity(new Location(0,0), new StringType(), "str_j"));
        FunctionEntity Str_GT = new FunctionEntity(new Location(0,0),new IntType(), "Str_GT", varList_gt, null);
        LibFunction.add(Str_GT);

        List<Entity> varList_le = new LinkedList<>();
        varList_add.add(new Entity(new Location(0,0), new StringType(), "str_i"));
        varList_add.add(new Entity(new Location(0,0), new StringType(), "str_j"));
        FunctionEntity Str_LE = new FunctionEntity(new Location(0,0),new IntType(), "Str_LE", varList_le, null);
        LibFunction.add(Str_LE);

        List<Entity> varList_ge = new LinkedList<>();
        varList_add.add(new Entity(new Location(0,0), new StringType(), "str_i"));
        varList_add.add(new Entity(new Location(0,0), new StringType(), "str_j"));
        FunctionEntity Str_GE = new FunctionEntity(new Location(0,0),new IntType(), "Str_GE", varList_ge, null);
        LibFunction.add(Str_GE);

    }

    public static List<FunctionEntity>  LibFunc() {

        return LibFunction;
    }

}
