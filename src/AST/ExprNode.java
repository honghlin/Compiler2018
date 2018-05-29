package AST;

import FrontEnd.ASTVisitor;
import IR.Operand.Operand;
import Type.Type;
import Entity.*;

import java.util.HashMap;

abstract public class ExprNode extends Node{

    private boolean isLvalue = false;
    abstract public Type type();
    private Operand operand;

    public ExprNode() {

        super();
    }

    public boolean isLvalue() {

        return isLvalue;
    }

    public void setLvalue(boolean isLvalue) {

        this.isLvalue = isLvalue;
    }

    public void setOperand(Operand operand) {//private

        this.operand = operand;
    }

    public Operand operand() {

        return operand;
    }

    abstract public void accept(ASTVisitor visitor);

    abstract public ExprNode Inline(HashMap<Entity, Operand> inlineMap);

    abstract public String hash();
}
