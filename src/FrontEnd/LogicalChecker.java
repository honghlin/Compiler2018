package FrontEnd;

import AST.*;

public class LogicalChecker extends Visitor {

    private boolean f = true;

    public boolean check(BinaryOpNode node) {

        switch(node.operator()) {

            case DIV: {

                if(!(node.right() instanceof IntegerLiteralNode)) f = false;
                break;
            }
            case MOD: {

                if(!(node.right() instanceof IntegerLiteralNode)) f = false;
                f = false;
                break;
            }
            //default: throw new Error();
        }

        visitExpr(node.left());
        visitExpr(node.right());

        return f;

    }

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

        check(node);
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

        check((BinaryOpNode) node);
    }
    @Override public void visit(LogicalOrNode node) {

        check((BinaryOpNode) node);
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

}