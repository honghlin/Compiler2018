package Entity;

import java.util.*;

import AST.*;//BlockNode
import IR.Ins;
import IR.Label;
import Optim.InlineChecker;
import Type.*;
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
    private int numOfVirtualReg = 16;//static
    private List<Reg> regList = new ArrayList<>();
    private List<Ins> insList =  new ArrayList<>();
    public int v; // varSize
    private boolean inlineMode = true;
    public boolean[] used = null;
    private boolean optim = false;
    public int MaxReg = 0;
    public boolean Rec = false;
    private boolean isInlined = false;
    private Set<FunctionEntity> calls = new HashSet<>();

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

    public int numOfVirtualReg() {

        return numOfVirtualReg;
    }

    public boolean inlineMode() {

        return inlineMode;
    }

    public void setInlineMode(boolean inlineMode) {

        this.inlineMode = inlineMode;
    }

    public void setOptim(boolean optim) {

        this.optim = optim;
    }

    public boolean isOptim() {

        return optim;
    }

    /*public void remove() {

        insList.remove(insList.size() - 1);
    }*/


    public Set<FunctionEntity> calls() {

        return calls;
    }

    public void addCall(FunctionEntity entity) {

        calls.add(entity);
    }

    private Map<FunctionEntity, Boolean> visited;

    private int stmtSize;

    public void checkInlinable() {

        InlineChecker checker = new InlineChecker(this);

        if(!checker.check()) isInlined = false;

        else if (name.equals("main")) isInlined = false;

            //else if (name.equals("origin")) isInlined = false;

            //else if (name.equals("printF")) isInlined = false;

        else {

            visited = new Hashtable<>();
            isInlined = !findcircle(this, this);
            stmtSize = stmtSize(body);
            if (stmtSize >  10) isInlined = false; // 8
            if (isInlined) System.err.println(name() + " is inlined");
        }
    }


    private int stmtSize(StmtNode node) {

        int count = 0;
        if (node == null) return 0;
        if (node instanceof BlockNode) {
            for (StmtNode stmtNode : ((BlockNode) node).stmts()) {
                if (stmtNode instanceof  BlockNode) count += stmtSize(stmtNode);
                else if (stmtNode instanceof ForNode) count += 3 + stmtSize(((ForNode) stmtNode).body());
                else if (stmtNode instanceof IfNode) count += 1 + stmtSize(((IfNode) stmtNode).elseBody()) + stmtSize(((IfNode) stmtNode).thenBody());
                else if (stmtNode instanceof WhileNode) count += 100 + stmtSize(((WhileNode) stmtNode).body()); // 1
                else ++count;
            }
        }
        else return 1;

        return count;
    }

    private int ifSize(StmtNode node) {

        int count = 0;
        if (node == null) return 0;
        if (node instanceof BlockNode) {
            for (StmtNode stmtNode : ((BlockNode) node).stmts()) {
                if (stmtNode instanceof  BlockNode) count += ifSize(stmtNode);
                if (stmtNode instanceof IfNode) count += 1 + ifSize(((IfNode) stmtNode).thenBody());
            }
        }
        else return 1;

        return count;
    }

    private boolean findcircle(FunctionEntity current, FunctionEntity called) {

        if (visited.containsKey(called)) return called == current;
        visited.put(called, true);
        for (FunctionEntity function : called.calls()) {

            if (findcircle(current, function)) return true;
        }
        return false;
    }

    public boolean isInlined() {

        return isInlined;
    }

    public void setInsList(List<Ins> insList) {

        this.insList = insList;
    }

    public boolean canbeSelfInline(int depth) {

        /*int count = 0;

        for (StmtNode stmtNode : body.stmts()) {

            if (stmtNode instanceof IfNode) count += 1;
        } count */

        if(ifSize(body) >= 4) return false;

        if (depth >= 3) return false;// 2  1    5 4
        int pow = 1;
        for (int i = 0; i < depth + 1; i++) pow *= stmtSize;
        return pow < 371; // 40 50 60 75 101
    }

    public boolean check() {

        return stmtSize(body) <= 10; //true
    }

}
