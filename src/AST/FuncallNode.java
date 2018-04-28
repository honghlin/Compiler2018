package AST;

import Type.Type;
import Type.FunctionType;

import java.util.List;

public class FuncallNode extends ExprNode {

    private ExprNode expr;
    private List<ExprNode> varList;

    public FuncallNode(ExprNode expr, List<ExprNode> exprList) {

        this.expr = expr;
        this.varList = exprList;
    }

    public ExprNode expr() {

        return expr;
    }

    public List<ExprNode> exprList() {

        return varList;
    }

    public void addThisPointer(ExprNode expr) {

        varList.add(0, expr);
    }

    @Override
    public Type type() {

        return functionType().entity().returnType();
    }

    public FunctionType functionType() {

        return (FunctionType)expr.type();
    }

    @Override
    public Location location() {

        return expr.location();
    }

    //@Override
    //public <S,E> E accept(ASTVisitor<S,E> visitor) {

    //    return visitor.visit(this);
    //}


}
