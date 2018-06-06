package Optim;

import Entity.Entity;
import FrontEnd.Visitor;

import AST.*;
import FrontEnd.Visitor;

import java.util.HashMap;
import java.util.HashSet;

public class BinaryChecker extends Visitor {

    boolean f = true;

    HashSet<Entity> set = new HashSet<>();

    @Override public void visit(ClassDefinitionNode node) {

        f = false;
        //return;
    }

    @Override public void visit(FunctionDefinitionNode node) {

        f = false;
    }

    @Override public void visit(VariableDefinitionNode node) {

        f = false;
    }

    @Override public void visit(AssignNode node) {

        f = false;
    }

    @Override public void visit(ArefNode node) {

        f = false;
    }
    @Override public void visit(BinaryOpNode node) {

        switch(node.operator()) {


            case ADD:    break;
            case SUB:    break;

            default: f = false;//
        }

        visitExpr(node.left());
        visitExpr(node.right());
    }

    @Override public void visit(BoolLiteralNode node) {

        return;
    }

    @Override public void visit(CreatorNode node) {

        f = false;
    }

    @Override public void visit(FuncallNode node) {

        f = false;
    }

    @Override public void visit(IntegerLiteralNode node) {

        return;
    }

    @Override public void visit(LogicalAndNode node) {

        f = false;
    }

    @Override public void visit(LogicalOrNode node) {

        f = false;
    }

    @Override public void visit(MemberNode node) {

        f = false;
    }

    @Override public void visit(PrefixOpNode node) {

        f = false;
    }

    @Override public void visit(StringLiteralNode node) {

        return;
    }

    @Override public void visit(SuffixOpNode node) {

        f = false;
    }

    @Override public void visit(UnaryOpNode node) {

        f = false;
    }

    @Override public void visit(VariableNode node) {

        if(!set.contains(node.entity())) set.add(node.entity());

        return;
    }

    @Override public void visit(BlockNode node) {

        f = false;
    }
    @Override public void visit(BreakNode node) {

        f = false;
    }
    @Override public void visit(ContinueNode node) {

        f = false;
    }

    @Override public void visit(ExprStmtNode node) {

        f = false;
    }

    @Override public void visit(ForNode node) {

        f = false;
    }
    @Override public void visit(IfNode node) {

        f = false;
    }

    @Override public void visit(ReturnNode node) {

        f = false;
    }

    @Override public void visit(WhileNode node) {

        f = false;
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

    public boolean check(BinaryOpNode node) {

        visit(node);
        if(set.size() > 3) return false;
        return f;
    }

}
