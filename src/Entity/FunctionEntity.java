package Entity;

import java.util.ArrayList;
import java.util.List;

import AST.BlockNode;
import IR.Ins;
import IR.Label;
import Type.Type;
import Type.FunctionType;
import AST.Location;
import Scope.Scope;
import IR.Operand.*;

public class FunctionEntity extends Entity{

    private Type returnType;
    private List<Entity> varList; //Parameter items tparams
    private BlockNode body;
    private Scope scope;
    private Label beginLabel, endLabel;
    static private int numOfVirtualReg = 16;
    private List<Reg> regList = new ArrayList<>();
    private List<Ins> insList =  new ArrayList<>();
    public int v; // varSize

    private boolean isConstructor = false;

    public FunctionEntity(Location loc, Type returnType, String name, List<Entity> varList, BlockNode body) {
        super(loc, new FunctionType(name), name);
        this.varList = varList;
        this.body = body;
        this.returnType = returnType;
        ((FunctionType)this.type).setEntity(this);
        for(int i = 0; i < 16; ++i) regList.add(PhiReg.getByRank(i));
    }

    public Entity addThisPointer(Location loc, ClassEntity entity) {
        Entity thisPointer = new Entity(loc, entity.type(), "this");
        varList.add(0, thisPointer);
        return thisPointer;
    }

    @Override
    public String name() {

        return name;
    }

    @Override
    public String toString() {

        return "function entity : " + name;
    }

    public List<Entity> varList() {

        return varList;
    }

    public BlockNode body() {

        return body;
    }

    public Scope scope() {

        return scope;
    }

    public void setScope(Scope scope) {

        this.scope = scope;
    }

    public Type returnType() {

        return returnType;
    }

    public boolean isConstructor() {

        return isConstructor;
    }

    public void setIsConstructor(boolean constructor) {

        isConstructor = constructor;
    }


    public void setLabel(Label beginLabel, Label endLabel) {

        this.beginLabel = beginLabel;
        this.endLabel = endLabel;
    }

    public Label beginLabel() {

        return beginLabel;
    }

    public Label endLabel() {

        return endLabel;
    }

    public VirReg newReg() {

        VirReg t = new VirReg(numOfVirtualReg++);
        regList.add(t);
        return t;
    }

    public void addIns(Ins ins) {

        insList.add(ins);
    }


    public List<Reg> regList() {

        return regList;
    }

    public List<Ins> insList() {
        return insList;
    }


}
