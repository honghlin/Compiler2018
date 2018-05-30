package Optim;

import AST.*;
import Entity.*;
import FrontEnd.Visitor;
import Type.*;

public class FunctionRecorder extends Visitor {

    FunctionEntity entity;
    boolean f = true;

    public FunctionRecorder(FunctionEntity entity) {

        this.entity = entity;

    }

    public boolean check() {

        visit(entity.body());
        if(!(entity.returnType() instanceof IntType) && !(entity.returnType() instanceof BoolType)) return false;
        if(entity.varList() == null || entity.varList().size() != 1) return false;
        if(!(entity.varList().get(0).type() instanceof IntType)) return false;
        return f;
    }

    @Override public void visit(VariableNode node) {

        if(node.entity().isGlobal()) f = false;
        return;
    }

    @Override public void visit(FuncallNode node) {

        if(!node.functionType().entity().name().equals(entity.name())) f = false;
    }


}
