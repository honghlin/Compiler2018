package AST;

import FrontEnd.ASTVisitor;
import Type.Type;
import Type.FunctionType;

import java.util.List;

public class FuncallNode extends ExprNode {

    private ExprNode expr;
    private List<ExprNode> varList;

    public FuncallNode(ExprNode expr, List<ExprNode> varList) {

        this.expr = expr;
        this.varList = varList;
    }

    public ExprNode expr() {

        return expr;
    }

    public List<ExprNode> varList() {

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

    @Override
    public void accept(ASTVisitor visitor) {

        visitor.visit(this);
    }

}
