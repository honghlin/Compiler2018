package FrontEnd;

import Entity.*;
import AST.*;
import Error.SemanticError;
import Scope.Scope;
import Type.*;

import java.util.Stack;

public class Initializer extends Visitor{

    private Stack<Scope> SymbolTable = new Stack<>();
    private Scope currentScope;
    private Scope globalScope;

    private ClassEntity currentClass = null;
    private Entity currentThis = null;

    public Initializer(Scope topScope) {

        SymbolTable.push(currentScope);
        this.globalScope = topScope;
        currentScope = topScope;
    }

    @Override public void visit(BlockNode node) {

        enter();
        node.setScope(currentScope);
        for(StmtNode s: node.stmts()) visitStmt(s);
        exit();
    }

    @Override public void visit(CreatorNode node) {

        if (!resolveType(node.type())) throw new SemanticError(node.location(), "Error Type 1");
        if (node.exprs() != null) for(ExprNode e: node.exprs()) visitExpr(e);
    }

    @Override public void visit(StringLiteralNode node) {

        Entity entity = globalScope.searchCurrently(node.value());
        if (entity == null) {
            entity = new StringConstantEntity(node.location(), new StringType(), node.value());
            globalScope.insert(entity);
        }
        node.setEntity((StringConstantEntity) entity);
    }

    @Override public void visit(VariableNode node) {
        Entity entity = currentScope.search(node.name());
        if (entity == null) throw new SemanticError(node.location(), "Error Type 2 ");
        node.setEntity(entity);
        if (currentClass != null && currentClass.scope().searchCurrently(node.name()) != null) node.setThisPointer(currentThis);
    }

    @Override public void visit(ClassDefinitionNode node) {

        ClassEntity entity = node.entity();
        enterClass(entity);
        for (VariableDefinitionNode memberVar : entity.memberVars()) currentScope.insert(memberVar.entity());
        for (FunctionDefinitionNode memberFunc : entity.memberFuncs()) currentScope.insert(memberFunc.entity());
        for(StmtNode s: entity.memberVars()) visitStmt(s);
        for(StmtNode s: entity.memberFuncs()) visitStmt(s);
        exitClass();
    }

    @Override public void visit(VariableDefinitionNode node) {

        VariableEntity entity = node.entity();
        if (!resolveType(entity.type())) throw new SemanticError(node.location(), "Type Error");
        if (currentClass == null || currentClass.scope() != currentScope) {
            if (entity.Expr() != null) visitExpr(entity.Expr());
            currentScope.insert(entity);
        }
    }

    @Override public void visit(FunctionDefinitionNode node) {

        FunctionEntity entity = node.entity();
        enter();
        entity.setScope(currentScope);
        if (!resolveType(entity.returnType())) throw new SemanticError(node.location(), "Error Type 3 ");
        if (currentClass != null) currentThis = entity.addThisPointer(node.location(), currentClass);
        for (Entity p : entity.varList()) {
            currentScope.insert(p);
            if (!resolveType(p.type())) throw new SemanticError(node.location(), "Error Type 4");
        }
        visit(entity.body());
        exit();
    }

    private void enter() {

        currentScope = new Scope(currentScope);
        SymbolTable.push(currentScope);
    }

    private void exit() {

        SymbolTable.pop();
        currentScope = SymbolTable.peek();
    }

    private void enterClass(ClassEntity entity) {
        currentClass = entity;
        enter();
        entity.setScope(currentScope);
    }

    private void exitClass() {
        exit();
        currentClass = null;
    }

    private boolean resolveType(Type type) {

        if (type instanceof ClassType) {
            ClassType t = (ClassType) type;
            Entity entity = currentScope.search(t.name());
            if (entity == null || !(entity instanceof ClassEntity)) return false;
            t.setEntity((ClassEntity)entity);
        }
        else if (type instanceof FunctionType) {
            FunctionType t = (FunctionType) type;
            Entity entity = currentScope.search(t.name());
            if (entity == null || !(entity instanceof FunctionEntity)) return false;
            t.setEntity((FunctionEntity)entity);
        }
        else if (type instanceof ArrayType) {
            ArrayType t = (ArrayType) type;
            return resolveType(t.baseType());
        }
        return true;
    }

}
