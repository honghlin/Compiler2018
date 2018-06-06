package Optim;

import AST.FunctionDefinitionNode;
import Entity.ClassEntity;
import Entity.FunctionEntity;
import IR.*;
import IR.Operand.GlobalAddr;
import IR.Operand.Imm;
import IR.Operand.PhiReg;
import backend.IRVisitor;

import java.util.*;

public class InsChecker implements IRVisitor {


    public InsChecker() { // IR ir

    }

    @Override public void visit(Assign ins) {

        if(ins.lhs() instanceof GlobalAddr) return;
        if(ins.next == null) return;
        if(ins.lhs() == PhiReg.rax) return;
        if(!ins.next.in.contains(ins.def)) ins.sel = false;

    }

    @Override public void visit(Binary ins) {

        if(ins.dest() instanceof GlobalAddr) return;
        if(ins.next == null) return;
        if(ins.dest() == PhiReg.rax) return;
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

            init(entity);
            init_0(entity);
            init_1(entity);
            select(entity);
        }

        for (ClassEntity entity : ir.ast().classEntities()) {
            for (FunctionDefinitionNode node : entity.memberFuncs()) {

                init(node.entity());
                init_0(node.entity());
                init_1(node.entity());
                select(node.entity());
            }
        }


    }

    private void select(FunctionEntity entity) {

        for(Ins ins : entity.insList()) visitIns(ins);

    }

    private void init(FunctionEntity entity) {

        List<Ins> now = entity.insList();
        List<Ins> newIns = new ArrayList<>();//N

        for(int i = 1; i < now.size(); ++i) {

            boolean f = false;
            if(now.get(i - 1) instanceof Binary && now.get(i) instanceof Cjump) {

                Binary fir =(Binary)now.get(i - 1);
                Cjump sec = (Cjump) now.get(i);
                if(fir.dest() == sec.left() && sec.right() instanceof Imm && ((Imm)sec.right()).value() == 1) {//!

                    Cjump.Type op = null;
                    switch (fir.op()) {

                        case LT: op = Cjump.Type.LT; break;
                        case LE: op = Cjump.Type.LE; break;
                        case GT: op = Cjump.Type.GT; break;
                        case GE: op = Cjump.Type.GE; break;
                        case EQ: op = Cjump.Type.EQ; break;
                        case NE: op = Cjump.Type.NE; break;
                        //default: throw new Error();
                    }

                    if(op != null && (sec.Op() == Cjump.Type.EQ || sec.Op() == Cjump.Type.LE)) { //

                        f = true;
                        ++i;
                        newIns.add(new Cjump(fir.left(), fir.right(), op, sec.TrueLabel()));
                    }
                }
            }
            if(f) continue;
            newIns.add(now.get(i - 1));
            if(i == now.size() - 1) newIns.add(now.get(i));
        }

        entity.setInsList(newIns);/**/
    }

    private void init_0(FunctionEntity entity) {

        List<Ins> now = entity.insList();
        List<Ins> newIns = new ArrayList<>();//N

        Set<Label> useLabel = new HashSet<>();
        HashMap<Label, Label> labelMap = new HashMap<>();
        for(Ins ins : now) {
            if(ins instanceof Label) labelMap.put((Label)ins, (Label)ins);
            if(ins instanceof Jump) useLabel.add(((Jump)ins).Label());
            if(ins instanceof Cjump) useLabel.add(((Cjump)ins).TrueLabel());
        }
        Ins pre = null;
        for(int i = 0; i < now.size(); ++i){

            Ins item = now.get(i);

            if(item instanceof Label && !useLabel.contains(item)) continue;
            if(pre instanceof Jump) {
                if(!(item instanceof Label)) continue;
            }
            if(pre instanceof Label) {
                if(item instanceof Label) {
                    labelMap.put((Label)item,(Label)pre);
                    continue;
                }
            }
            if(pre != null) newIns.add(pre);
            pre = item;
        }
        if(pre != null) newIns.add(pre);
        for(Ins item : newIns) {
            if(item instanceof Jump) {
                Label tmp = ((Jump) item).Label();
                for(Label next = labelMap.get(tmp); next != tmp; tmp = next, next = labelMap.get(tmp)) ;
                ((Jump)item).setLabel(tmp);
            }
            if(item instanceof Cjump) {
                Label tmp = ((Cjump)item).TrueLabel();
                for(Label next = labelMap.get(tmp); next != tmp; tmp = next, next = labelMap.get(tmp));
                ((Cjump)item).setTrueLabel(tmp);
            }
        }
        entity.setInsList(newIns);/**/
    }

    private void init_1(FunctionEntity entity) {

        List<Ins> now = entity.insList();
        List<Ins> newIns = new ArrayList<>();//N

        for(int i = 0; i < now.size() - 1; ++i) {

            if(now.get(i) instanceof Jump && now.get(i + 1) instanceof Label) { //  &&

                Jump fir =(Jump)now.get(i);
                Label sec = (Label) now.get(i + 1);
                if(fir.Label() == sec) {
                    newIns.add(sec);
                    ++i;
                    continue;
                }
            }

            newIns.add(now.get(i));
            if(i == now.size() - 2) newIns.add(now.get(i + 1));
        }

        entity.setInsList(newIns);/**/
    }

}
