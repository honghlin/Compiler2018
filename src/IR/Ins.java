package IR;

import IR.Operand.*;
import backend.IRVisitor;

import java.util.HashSet;
import java.util.Set;

abstract public class Ins {

    public Reg def = null;

    public Set<Reg> in = new HashSet<>();

    public Ins next;

    public boolean sel = true;   //S

    abstract public String toString();

    abstract public void accept(IRVisitor visitor);

}
