package backend;

import IR.*;
import IR.Operand.*;

import java.util.List;
import java.util.Set;



public class LivenessAnalyzer implements IRVisitor{

    //private List<Ins> insSet;

    public void visitIns(Ins ins) {//private

        ins.accept(this);
    }

    public void visit(Assign o) {

        setDef(o, o.lhs());
        setAssign(o.in, o.lhs());
        setUse(o.in, o.rhs());
    }

    public void visit(Binary o) {

        setDef(o, o.dest());
        setAssign(o.in, o.dest());
        setUse(o.in, o.left());
        setUse(o.in, o.right());
    }

    public void visit(Call o) {

        for(Ins item : o.INS()) visitIns(item);
    }

    public void visit(Cjump o) {

        setUse(o.in, o.left());
        setUse(o.in, o.right());
    }

    public void visit(Jump o) {

    }

    public void visit(Label o) {

    }

    public void visit(Unary o) {

        setDef(o, o.Src());
        setUse(o.in, o.Src());
    }

    public void visit(Funcall o) {

        int k = o.size() > 6 ? 6 : o.size();
        setDef(o, PhiReg.rax);
        setAssign(o.in, PhiReg.rax);
        for (int i = 0; i < k; ++i) setUse(o.in, PhiReg.getParameterReg(i));
    }

    private void setDef(Ins ins, Operand o) {

        if(o instanceof Reg) ins.def = (Reg)o;
    }


    private void setUse(Set<Reg> in, Operand o) {

        if(o instanceof Reg) in.add((Reg)o);
        else setAssign(in, o);
    }

    private void setAssign(Set<Reg> in, Operand o) {

        if(o instanceof Mem) {
            if(((Mem)o).base() != null) in.add(((Mem)o).base());
            if(((Mem)o).index() != null) in.add(((Mem)o).index());
        }
    }

}
