package Optim;

import AST.*;
import Entity.Entity;
import Entity.FunctionEntity;
import FrontEnd.ASTVisitor;
import FrontEnd.Visitor;
import FrontEnd.AST;
import IR.*;
import Type.ArrayType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class IrrelevantMaker extends Visitor {

    private boolean setMode = true;
    private boolean _setMode = true;

    private List<Entity> set = new ArrayList<>();

    private List<ExprNode> trueSet = new ArrayList<>(); //LinkedList

    @Override public void visit(FunctionDefinitionNode node) {

        visitStmt(node.entity().body());
    }

    @Override public void visit(VariableDefinitionNode node) {

        if (node.entity().Expr() != null)  visitExpr(node.entity().Expr());
    }

    @Override public void visit(AssignNode node) {

        setMode = false;//
        visitExpr(node.lhs());
        setMode = true;//
        visitExpr(node.rhs());

        if(node.rhs() instanceof VariableNode && ((VariableNode) node.rhs()).entity().type() instanceof ArrayType) { ;  // alias ArefNode !()

            VariableNode n = deepNode(node.lhs());
            set.add(n.entity());//

        }
        else {

            VariableNode n = deepNode(node.lhs());
            //trueSet.add(n.entity()); ((VariableNode)n).entity().setIrrelevant(true);
        }

    }

    @Override public void visit(ArefNode node) {

        visitExpr(node.expr());
        //if(!setMode) setMode = true;
        visitExpr(node.index());
        //setMode = false;
        trueSet.add(node.index());
    }
    @Override public void visit(BinaryOpNode node) {

        //setMode = false;
        visitExpr(node.left());
        //setMode = true;
        visitExpr(node.right());
    }

    @Override public void visit(BoolLiteralNode node) {

        return;
    }

    @Override public void visit(CreatorNode node) {

        if (node.exprs() != null) for(ExprNode e: node.exprs()) visitExpr(e);
    }

    @Override public void visit(FuncallNode node) {

        visitExpr(node.expr());
        if(node.varList() != null) for(ExprNode e: node.varList()) visitExpr(e);
    }

    @Override public void visit(IntegerLiteralNode node) {

        return;
    }

    @Override public void visit(LogicalAndNode node) {

        visitExpr(node.left());
        visitExpr(node.right());
    }
    @Override public void visit(LogicalOrNode node) {

        visitExpr(node.left());
        visitExpr(node.right());
    }

    @Override public void visit(MemberNode node) {

        visitExpr(node.expr());
    }

    @Override public void visit(PrefixOpNode node) {

        visitExpr(node.expr());
    }

    @Override public void visit(StringLiteralNode node) {

        return;
    }

    @Override public void visit(SuffixOpNode node) {

        visitExpr(node.expr());
    }

    @Override public void visit(UnaryOpNode node) {

        visitExpr(node.expr());
    }

    @Override public void visit(VariableNode node) {

        if(_setMode || setMode) node.entity().setIrrelevant(false);//
        return;
    }

    @Override public void visit(BlockNode node) {

        if(node.stmts() != null) for(StmtNode s: node.stmts()) visitStmt(s);
    }
    @Override public void visit(BreakNode node) {

        return;
    }
    @Override public void visit(ContinueNode node) {

        return;
    }

    @Override public void visit(ExprStmtNode node) {

        visitExpr(node.expr());
    }

    @Override public void visit(ForNode node) {

        if (node.init() != null) visitExpr(node.init());
        if (node.cond() != null) visitExpr(node.cond());
        if (node.incr() != null) visitExpr(node.incr());
        if (node.body() != null) visitStmt(node.body());
    }
    @Override public void visit(IfNode node) {

        visitExpr(node.cond());
        if (node.thenBody() != null) visitStmt(node.thenBody());
        if (node.elseBody() != null) visitStmt(node.elseBody());
    }

    @Override public void visit(ReturnNode node) {

        if (node.expr() != null) visitExpr(node.expr());
    }

    @Override public void visit(WhileNode node) {

        visitExpr(node.cond());
        if (node.body() != null) visitStmt(node.body());
    }

    public void visitStmt(StmtNode s) {

        s.accept(this);
    }

    public void visitExpr(ExprNode e) {

        e.accept(this);
    }

    public void visitDefinition(DefinitionNode def) {

        def.accept(this);
    }

    public void check(AST ast) {

        _setMode = false;

        for (FunctionEntity entity : ast.functionEntities()) visit(entity.body());

        _setMode = true;

        // for(ExprNode node : trueSet) visitExpr(node);//entity.setIrrelevant(true)

        int size = trueSet.size();

        for(int i = 0; i <= size - 1; ++i) {

            ExprNode node = trueSet.get(i);
            visitExpr(node);
        }

        for(Entity entity : set) entity.setIrrelevant(false); // ((VariableNode)n). ()

    }

    private VariableNode deepNode(ExprNode node) {

        if(node instanceof VariableNode) return (VariableNode) node;
        else if(node instanceof ArefNode) return deepNode(((ArefNode)node).expr());
        else throw new Error();
    }

}
