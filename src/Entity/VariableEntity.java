package Entity;

import AST.ExprNode;
import AST.Location;
import Type.Type;

public class VariableEntity extends Entity {

    private ExprNode Expr;

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

}
