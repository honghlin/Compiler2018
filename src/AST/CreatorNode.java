package AST;

import java.util.List;
import Type.Type;

public class CreatorNode extends ExprNode {

    private Location location;
    private Type type;
    private List<ExprNode> exprs;

    public CreatorNode(Location loc, Type type, List<ExprNode> exprs) {

        this.location = loc;
        this.type = type;
        this.exprs = exprs;
    }

    @Override
    public Type type() {

        return type;
    }

    public List<ExprNode> exprs() {

        return exprs;
    }

    public void setExprs(List<ExprNode> exprs) {

        this.exprs = exprs;
    }

    @Override
    public Location location() {

        return location;
    }

    //@Override
    //public <S,E> E accept(ASTVisitor<S,E> visitor) {

    //    return visitor.visit(this);
    //}

}
