package FrontEnd;
import AST.*;

abstract public class Visitor implements ASTVisitor {

    @Override public void visit(ClassDefinitionNode node) {

        for(StmtNode s: node.entity().memberVars()) visitStmt(s);
        for(StmtNode s: node.entity().memberFuncs()) visitStmt(s);
    }

    @Override public void visit(FunctionDefinitionNode node) {

        visitStmt(node.entity().body());
    }

    @Override public void visit(VariableDefinitionNode node) {

        if (node.entity().Expr() != null)  visitExpr(node.entity().Expr());
    }

    @Override public void visit(AssignNode node) {

        visitExpr(node.lhs());
        visitExpr(node.rhs());
    }

    @Override public void visit(ArefNode node) {

        visitExpr(node.expr());
        visitExpr(node.index());
    }
    @Override public void visit(BinaryOpNode node) {

        visitExpr(node.left());
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
        for(ExprNode e: node.varList()) visitExpr(e);
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

        return;
    }

    @Override public void visit(BlockNode node) {

        for(StmtNode s: node.stmts()) visitStmt(s);
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

}
