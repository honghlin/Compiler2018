package AST;

import Type.Type;

abstract public class ExprNode extends Node{

    private boolean isLvalue = false;
    abstract public Type type();

    public ExprNode() {

        super();
    }

    public boolean isLvalue() {

        return isLvalue;
    }

    public void setLvalue(boolean isLvalue) {

        this.isLvalue = isLvalue;
    }

    //abstract public <S,E> E accept(ASTVisitor<S,E> visitor);

}
