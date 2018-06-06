package Optim;

import AST.*;
import Entity.*;
import FrontEnd.Visitor;
import Type.*;




public class InlineChecker extends Visitor {

    FunctionEntity entity;
    boolean f = true;

    public InlineChecker(FunctionEntity entity) {

        this.entity = entity;

    }

    public boolean check() {

        visit(entity.body());
        return f;
    }

    @Override public void visit(IfNode node) {

        f = false;
    }

    @Override public void visit(FuncallNode node) {

        FunctionEntity entity = node.functionType().entity();
        if(entity.name().equals("println") || entity.name().equals("print")) f = false;
    }

    @Override public void visit(CreatorNode node) {

        f = false;
    }
}
