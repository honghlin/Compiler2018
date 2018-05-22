package IR;

import Entity.FunctionEntity;
import IR.Operand.Mem;
import IR.Operand.Operand;
import IR.Operand.PhiReg;

import java.util.ArrayList;
import java.util.List;

public class Call extends Ins {

    //private int size;
    private FunctionEntity entity;
    private List<Ins> INS = new ArrayList<>();

    public Call(FunctionEntity entity, List<Operand> params, Operand dest) {

        this.entity = entity;
        for(int i = params.size(); i >= 1; --i) {
            Operand t = params.get(i - 1);
            if(i > 6) INS.add(new Assign(new Mem(PhiReg.rsp, null, 0, (i - 7) * 8), t));
            else INS.add(new Assign(PhiReg.getParameterReg(i - 1), t));
        }
        INS.add(new Funcall(entity, params.size()));
        INS.add(new Assign(dest, PhiReg.rax));
    }

    @Override public String toString() {
        String t = "";
        for(Ins item : INS) t += item.toString();
        return t;
    }

    public FunctionEntity entity() {
        return entity;
    }

    public List<Ins> INS() {
        return INS;
    }

}
