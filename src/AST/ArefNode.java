package AST;

import Type.Type;
import FrontEnd.ASTVistor;

public class ArefNode extends LHSNode {

    private ExprNode expr, index; // expr[index]

    public ArefNode(ExprNode expr, ExprNode index) {

        this.expr = expr;
        this.index = index;
    }

    public ArefNode(ExprNode expr, ExprNode index, Type type) {

        this.expr = expr;
        this.index = index;
        this.type = type;
    }

    public ExprNode expr() {

        return expr;
    }

    public ExprNode index() {

        return index;
    }

    public Location location() {

        return expr.location();
    }

    //public <S, E> E accept(ASTVisitor<S,E> visitor) {

      //  return visitor.visit(this);
    //}

}
