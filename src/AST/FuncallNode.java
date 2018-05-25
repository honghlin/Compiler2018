package AST;

import Entity.Entity;
import FrontEnd.ASTVisitor;
import IR.Operand.Operand;
import Type.Type;
import Type.FunctionType;
import IR.Label;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FuncallNode extends ExprNode {

    private ExprNode expr;
    private List<ExprNode> varList;
    private Label label;

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

    public Label label() {

        return label;
    }

    public void setLabel(Label label) {

        this.label = label;
    }

 /*   public FunctionType functionType() {

        return (FunctionType)expr.type();
    }*/

    @Override public FuncallNode Inline(HashMap<Entity, Operand> inlineMap) {

        FuncallNode node = new FuncallNode(this.expr, this.varList);
        node.expr = expr.Inline(inlineMap);
        node.varList = new ArrayList<>();
        for(ExprNode item: varList) node.varList.add(item.Inline(inlineMap));
        return node;
    }

}
