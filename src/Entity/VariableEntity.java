package Entity;

import AST.ExprNode;
import AST.Location;
import IR.Operand.GlobalAddr;
import IR.Operand.Operand;
import Type.Type;

public class VariableEntity extends Entity {

    private ExprNode Expr; // public
    private int offset;//just for class
    private Operand pos;
    private boolean IsGlobal = false;

    public VariableEntity(Location loc, Type type, String name, ExprNode expr) {

        super(loc, type, name);
        Expr = expr;
    }


    public VariableEntity(VariableEntity entity) {

        super(entity.location(), entity.type(), entity.name());
        Expr = entity.Expr();
    }


    public ExprNode Expr() {

        return Expr;
    }

    @Override
    public String toString() {

        return "variable entity : " + name;
    }

    public void SetOffset(int offect) {

        this.offset = offect;
    }

    public int Offset() {

        return offset;
    }

    public void setPos(Operand pos) {//private

       this.pos = pos;
    }

    public Operand pos() {

        return pos;
    }

    public boolean isGlobal() {
        return IsGlobal;
    }

    public void setIsGlobal(boolean IsGlobal) {

        this.IsGlobal = IsGlobal;
    }

    public void setExpr(ExprNode node) {

        this.Expr = node;
    }

}
