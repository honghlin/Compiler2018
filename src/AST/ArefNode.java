package AST;

import Entity.Entity;
import IR.Operand.Operand;
import Type.Type;
import FrontEnd.ASTVisitor;

import java.util.HashMap;

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

    public void accept(ASTVisitor visitor) {

        //if(expr == null) System.out.println(111);
        //if(index == null) System.out.println(111);
        visitor.visit(this);
    }

    @Override public ExprNode Inline(HashMap<Entity, Operand> inlineMap) {
        ArefNode node = new ArefNode(this.expr, this.index);
        node.expr = expr.Inline(inlineMap);
        node.index = expr.Inline(inlineMap);
        return node;
    }

}
