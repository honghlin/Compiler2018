package IR;

import backend.IRVisitor;

abstract public class Ins {

    abstract public String toString();

    abstract public void accept(IRVisitor visitor);

}
