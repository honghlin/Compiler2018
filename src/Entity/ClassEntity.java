package Entity;

//import Entity.Entity

import AST.Location;
import AST.VariableDefinitionNode;
import AST.FunctionDefinitionNode;
import Type.ClassType;
import Scope.Scope;

import java.util.List;

public class ClassEntity extends Entity {

    private List<VariableDefinitionNode> memberVars;
    private List<FunctionDefinitionNode> memberFuncs;

    private Scope scope;

    private FunctionEntity constructor;// constructor funtion
    private ClassType classType;

    public ClassEntity (Location loc, String name, List<VariableDefinitionNode> memberVars, List<FunctionDefinitionNode> memberFuncs) {

        super(loc, new ClassType(name), name);
        this.memberVars = memberVars;
        this.memberFuncs = memberFuncs;
        this.scope = null;
        this.constructor = null;
        ((ClassType)this.type).setEntity(this);
    }

    public ClassEntity (Location loc, String name, List<VariableDefinitionNode> memberVars, List<FunctionDefinitionNode> memberFuncs, FunctionEntity constructor) {

        super(loc, new ClassType(name), name);
        this.memberVars = memberVars;
        this.memberFuncs = memberFuncs;
        this.scope = null;
        this.constructor = constructor;
        ((ClassType)this.type).setEntity(this);
    }

    public List<VariableDefinitionNode> memberVars() {

        return memberVars;
    }

    public List<FunctionDefinitionNode> memberFuncs() {

        return memberFuncs;
    }

    public Scope scope() {

        return scope;
    }

    public void setScope(Scope scope) {

        this.scope = scope;
    }

    public FunctionEntity constructor() {

        return constructor;
    }

    public void setConstructor(FunctionEntity constructor) {

        this.constructor = constructor;
    }

    @Override
    public String toString() {

        return "class entity : " + name;
    }

}
