package FrontEnd;
import Entity.*;
import Type.*;
import AST.*;
import Scope.Scope;
import Error.SemanticError;

import java.util.List;

public class SemanticChecker extends Visitor {

    private int Depth = 0;
    private Scope scope;
    private TypeChecker typeChecker = new TypeChecker();

    static final Type boolType = new BoolType();
    static final Type intType = new IntType();
    static final Type stringType = new StringType();

    public SemanticChecker (Scope scope) {

        this.scope = scope;
    }

    @Override public void visit(IfNode node) {

        visitExpr(node.cond());
        if (node.thenBody() != null) visitStmt(node.thenBody());
        if (node.elseBody() != null) visitStmt(node.elseBody());
        typeChecker.checkType(node.location(), node.cond().type(), boolType);
    }

    @Override public void visit(WhileNode node) {
        visitExpr(node.cond());
        if (node.body() != null) {
            ++Depth;
            visitStmt(node.body());
            --Depth;
        }
        typeChecker.checkType(node.location(), node.cond().type(), boolType);
    }

    @Override public void visit(ForNode node) {

        if (node.init() != null) visitExpr(node.init());
        if (node.cond() != null) {
            visitExpr(node.cond());
            typeChecker.checkType(node.location(), node.cond().type(), boolType);
        }
        if (node.incr() != null) visitExpr(node.incr());
        if (node.body() != null) {
            ++Depth;
            visitStmt(node.body());
            --Depth;
        }
    }

    @Override public void visit(BreakNode node) {

        if (Depth <= 0) throw new SemanticError(node.location(), "break");
    }

    @Override public void visit(ContinueNode node) {

        if (Depth <= 0) throw new SemanticError(node.location(), "continue");
    }

    private FunctionEntity currentFunction = null;

    @Override public void visit(ReturnNode node) {
        if (currentFunction == null) throw new SemanticError(node.location(), "return but not Function");
        if (!currentFunction.isConstructor()) {
            if (node.expr() != null) {
                visitExpr(node.expr());
                typeChecker.checkType(node.location(), node.expr().type(), currentFunction.returnType());
            }
            else if (!currentFunction.returnType().isVoid()) throw new SemanticError(node.location(), "return void");
        }
        else if (node.expr() != null) throw new SemanticError(node.location(), "return in constructor");
    }

    @Override public void visit(AssignNode node) {

        visitExpr(node.lhs());
        visitExpr(node.rhs());
        if (!node.lhs().isLvalue()) throw new SemanticError(node.location(), "assign Rvalue");
        typeChecker.checkType(node.location(), node.lhs().type(), node.rhs().type());
    }

    @Override public void visit(VariableDefinitionNode node) {
        ExprNode init = node.entity().Expr();
        if (init != null) {
            visitExpr(init);
            typeChecker.checkType(node.location(), init.type(), node.entity().type());
        }
        if (node.entity().type().isVoid()) throw new SemanticError(node.location(), "set void");
    }

    @Override public void visit(FunctionDefinitionNode node) {

        currentFunction = node.entity();
        if (currentFunction.returnType() == null && !currentFunction.isConstructor()) throw new SemanticError(node.location(), "no return");
        visitStmt(node.entity().body());
        currentFunction = null;
    }

    @Override public void visit(ArefNode node) {

        visitExpr(node.expr());
        visitExpr(node.index());
        if (!node.expr().type().isArray()) throw new SemanticError(node.location(), "not array");
        typeChecker.checkType(node.index().location(), node.index().type(), intType);
        node.setType(((ArrayType)(node.expr().type())).baseType());
    }

    @Override
    public void visit(CreatorNode node) {

        if (node.exprs() != null) {
            for (ExprNode expr : node.exprs()) {
                visitExpr(expr);
                typeChecker.checkType(expr.location(), expr.type(), intType);
            }
        }
    }

    @Override public void visit(UnaryOpNode node) {
        Type B;
        visitExpr(node.expr());
        switch (node.operator()) {

            case L_NOT:
                B = boolType; break;

            case PRE_INC:
            case PRE_DEC:
            case SUF_INC:
            case SUF_DEC:
            case ADD:
            case MINUS:
            case B_NOT:
                B = intType; break;

            default: throw new Error();
        }
        typeChecker.checkType(node.location(), node.expr().type(), B);
    }

    @Override public void visit(PrefixOpNode node) {

        visit((UnaryOpNode)node);
        if (node.operator() == UnaryOpNode.UnaryOp.PRE_INC || node.operator() == UnaryOpNode.UnaryOp.PRE_DEC) node.setLvalue(true);
    }

    @Override
    public void visit(SuffixOpNode node) {

        visit((UnaryOpNode)node);
        if (!node.expr().isLvalue()) throw new SemanticError(node.location(), "lvalue is needed");
    }

    @Override public void visit(LogicalAndNode node) {

        visit((BinaryOpNode)node);
    }

    @Override public void visit(LogicalOrNode node) {

        visit((BinaryOpNode)node);
    }

    @Override
    public void visit(BinaryOpNode node) {

        visitExpr(node.left());
        visitExpr(node.right());

        Type L = node.left().type(), R = node.right().type();
        switch(node.operator()) {

            case ADD:

                typeChecker.checkType(node.location(), L, R);
                if (!L.isInt() && !L.isString()) throw new SemanticError(node.location(), "Add Error");
                node.setType(L);
                break;

            case GT:
            case LE:
            case GE:
            case LT:

                typeChecker.checkType(node.location(), L, R);
                if (!L.isInt() && !L.isString()) throw new SemanticError(node.location(), "Add Error");
                node.setType(boolType);
                break;

            case SUB :
            case MUL :
            case DIV :
            case MOD :
            case LSHIFT :
            case RSHIFT :
            case B_AND :
            case B_XOR :
            case B_OR :

                typeChecker.checkType(node.left().location(), L, intType);
                typeChecker.checkType(node.right().location(), R, intType);
                node.setType(intType);
                break;

            case EQ: case NE:

                typeChecker.checkType(node.location(), L, R);
                node.setType(boolType);// I think there are some problems here.
                break;

            case L_AND:
            case L_OR:

                typeChecker.checkType(node.left().location(), L, boolType);
                typeChecker.checkType(node.right().location(), R, boolType);
                node.setType(boolType);
                break;

            default: throw new Error();

        }

    }

    @Override public void visit(MemberNode node) {

        visitExpr(node.expr());
        Type t = node.expr().type();
        if (t.isClass()) {
            ClassEntity entity = ((ClassType) t).entity();
            Entity member = entity.scope().searchCurrently(node.member());
            if (member == null) throw new SemanticError(node.location(), "Error member");
            node.setEntity(member);
            node.setType(member.type());
        }
        else if (t.isArray()){
            Entity member = ArrayType.scope().search(node.member());
            if (member == null) throw new SemanticError(node.location(), "Error Member");
            node.setEntity(member);
            node.setType(member.type());
        }
        else if (t.isString()){
            Entity member = StringType.scope().search(node.member());
            if (member == null) throw new SemanticError(node.location(), "Error Member");
            node.setEntity(member);
            node.setType(member.type());
        }
        else throw new SemanticError(node.location(), "Error Member");

    }

    @Override public void visit(FuncallNode node) {

        visitExpr(node.expr());
        Type t = node.expr().type();
        if (!t.isFunction()) throw new SemanticError(node.location(), "Not Function");
        FunctionEntity entity = ((FunctionType)t).entity();
        List<Entity> pars = entity.varList();
        List<ExprNode> exprs = node.varList();
        int f = 0;
        if ((node.expr() instanceof MemberNode) || (node.expr() instanceof VariableNode && ((VariableNode)node.expr()).isMember())) f = 1;
        if (pars.size() - f != exprs.size()) throw new SemanticError(node.location(), "parameter Error");
        for (int i = f; i < pars.size(); ++i) {
            ExprNode expr = exprs.get(i - f);
            visitExpr(expr);
            typeChecker.checkType(expr.location(), expr.type(), pars.get(i).type());
        }
    }

}
