package Optim;

import AST.*;
import Entity.*;
import FrontEnd.Visitor;
import Type.*;

public class FunctionRecorder extends Visitor {

    FunctionEntity entity;
    boolean f = true;
    //boolean fin = false;

    public FunctionRecorder(FunctionEntity entity) {

        this.entity = entity;

    }

    public boolean check() {

        visit(entity.body());
        if(!(entity.returnType() instanceof IntType) && !(entity.returnType() instanceof BoolType)) return false;
        if(entity.varList() == null || entity.varList().size() != 1) return false;
        if(!(entity.varList().get(0).type() instanceof IntType)) return false;
        //fin = f;
        return f;
    }

    @Override public void visit(VariableNode node) {

        if(node.entity().isGlobal()) f = false;
        return;
    }

    @Override public void visit(FuncallNode node) {

        FunctionEntity other = node.functionType().entity(); //entity type()
        if(other.body() != null && other.body().stmts() != null && other.body().stmts().size() == 1 && other.body().stmts().get(0) instanceof ReturnNode);
        else if(!node.functionType().entity().name().equals(entity.name())) f = false;// && !fin
    }

    @Override public void visit(BinaryOpNode node) {

        switch(node.operator()) {

            case DIV: f = false; break;
            case MOD: f = false; break;
            case LSHIFT:  f = false;  break;
            case RSHIFT:  f = false;  break;
            case B_AND: f = false; break;
            case B_XOR: f = false; break;
            case B_OR:  f = false;  break;
            //default: throw new Error();
        }

        visitExpr(node.left());
        visitExpr(node.right());

    }

}
