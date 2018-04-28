package AST;

import FrontEnd.ASTVisitor;
import Type.Type;

public class AssignNode extends ExprNode{

    private ExprNode lhs, rhs;

    public AssignNode(ExprNode lhs, ExprNode rhs) {

        super();
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public ExprNode lhs() {

        return lhs;
    }

    public void setLhs(ExprNode lhs) {

        this.lhs = lhs;
    }

    public ExprNode rhs() {

        return rhs;
    }

    public void setRhs(ExprNode rhs) {

        this.rhs = rhs;
    }

    @Override
    public Type type() {

        return lhs.type();
    }

    @Override
    public Location location() {

        return lhs.location();
    }

    @Override
    public void accept(ASTVisitor visitor) {

        visitor.visit(this);
    }

}
