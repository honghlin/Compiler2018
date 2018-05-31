package Optim;

import AST.FunctionDefinitionNode;
import Entity.ClassEntity;
import Entity.FunctionEntity;
import IR.*;
import IR.Operand.GlobalAddr;
import backend.IRVisitor;

public class InsChecker implements IRVisitor {


    public InsChecker() { // IR ir

    }

    @Override public void visit(Assign ins) {

        if(ins.lhs() instanceof GlobalAddr) return;
        if(ins.next == null) return;
        if(!ins.next.in.contains(ins.def)) ins.sel = false;

    }

    @Override public void visit(Binary ins) {

        if(ins.dest() instanceof GlobalAddr) return;
        if(ins.next == null) return;
        if(!ins.next.in.contains(ins.def)) ins.sel = false;

    }

    @Override public void visit(Call ins) {

    }

    @Override public void visit(Cjump ins) {

    }

    @Override public void visit(Funcall ins) {

    }

    //void public visit(Ins ins){
    //
    // }

    @Override public void visit(Jump ins) {

    }

    @Override public void visit(Label ins) {

    }

    @Override public void visit(Unary ins) {

    }

    public void visitIns(Ins ins) {//private

        ins.accept(this);
    }

    public void check(IR ir) {

        for (FunctionEntity entity : ir.ast().functionEntities()) {
            select(entity);
        }

        for (ClassEntity entity : ir.ast().classEntities()) {
            for (FunctionDefinitionNode node : entity.memberFuncs()) {
                select(node.entity());
            }
        }


    }

    private void select(FunctionEntity entity) {

        for(Ins ins : entity.insList()) visitIns(ins);

    }

}
