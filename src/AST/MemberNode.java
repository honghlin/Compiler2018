package AST;

import Entity.Entity;
import FrontEnd.ASTVisitor;

public class MemberNode extends LHSNode{

    private ExprNode expr;
    private String member; //expr.member

    private Entity entity;

    public MemberNode(ExprNode expr, String member) {

        this.expr = expr;
        this.member = member;
    }

    public ExprNode expr() {

        return expr;
    }

    public String member() {

        return member;
    }

    public Entity entity() {

        return entity;
    }

    public void setEntity(Entity entity) {

        this.entity = entity;
    }

    @Override
    public boolean isLvalue() {

        return !entity.type().isFunction();
    }

    @Override
    public Location location() {

        return expr.location();
    }

    @Override
    public void accept(ASTVisitor visitor) {

        visitor.visit(this);
    }

}
