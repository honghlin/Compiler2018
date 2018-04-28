package AST;

import Type.Type;

abstract public class LHSNode extends ExprNode {

    protected Type type;

    @Override
    public Type type() {

        return type;
    }

    public void setType(Type t) {

        this.type = t;
    }

    @Override
    public boolean isLvalue() {

        return true;
    }

}
