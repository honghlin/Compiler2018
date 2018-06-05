package AST;

import java.util.HashMap;
import java.util.List;

import Entity.Entity;
import FrontEnd.ASTVisitor;
import IR.Operand.Operand;
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

    @Override public Type type() {

        return type;
    }

    public List<ExprNode> exprs() {

        return exprs;
    }

    public void setExprs(List<ExprNode> exprs) {

        this.exprs = exprs;
    }

    @Override public Location location() {

        return location;
    }

    @Override public void accept(ASTVisitor visitor) {

        visitor.visit(this);
    }

    @Override public ExprNode copy() {

        return new CreatorNode(this.location, this.type, this.exprs);
    }

    @Override public String hash() {

        String t = "";
        return t;
    }

}
