package backend;

import FrontEnd.AST;
import AST.*;
import Entity.*;
import FrontEnd.Visitor;
import IR.*;
import IR.Operand.*;
import Type.*;
//import org.graalvm.compiler.lir.Variable;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static IR.Binary.BinaryOp.*;

public class IRBuilder extends Visitor {

    private AST ast;
    private IR Ir = new IR();
    private FunctionEntity currentFunction = null;
    private Label continueLabel, breakLabel, returnLabel;

    private FunctionEntity malloc, Str_ADD, Str_EQ, Str_NE, Str_LT, Str_GT, Str_LE, Str_GE;

    public IRBuilder(AST ast) {

        this.ast = ast;
        malloc = (FunctionEntity) ast.scope().searchCurrently("malloc");
        Str_ADD = (FunctionEntity) ast.scope().searchCurrently("Str_ADD");
        Str_EQ = (FunctionEntity) ast.scope().searchCurrently("Str_EQ");
        Str_NE = (FunctionEntity) ast.scope().searchCurrently("Str_NE");
        Str_LT = (FunctionEntity) ast.scope().searchCurrently("Str_LT");
        Str_GT = (FunctionEntity) ast.scope().searchCurrently("Str_GT");
        Str_LE = (FunctionEntity) ast.scope().searchCurrently("Str_LE");
        Str_GE = (FunctionEntity) ast.scope().searchCurrently("Str_GE");

    }

    public IR generateIR() {
        Ir.ast = ast;
        for (ClassEntity entity : ast.classEntities()) entity.setOffset();
        for (VariableEntity entity : ast.variableEntities()) {
            if(entity.type() instanceof StringType) continue;
            Ir.globalInitializer.add(entity);
            entity.setIsGlobal(true);
            entity.setPos(new GlobalAddr(entity.name() + "__", false));
        }
        for (FunctionEntity entity : ast.functionEntities()) {
            currentFunction = entity;
            compileFunction(entity);
        }

        for (ClassEntity entity : ast.classEntities()) {
            for (FunctionDefinitionNode node : entity.memberFuncs()) {
                currentFunction = node.entity();
                compileFunction(node.entity());
            }
        }

        return Ir;
    }

    private void compileFunction(FunctionEntity entity) {

        Label begin = new Label(entity.name());
        Label end = new Label();
        entity.setLabel(begin, end);
        currentFunction = entity;
        for(int i = 0; i < entity.varList().size(); ++i) {
            Operand s;
            if(i < 6) {
                Entity t = (Entity)entity.varList().get(i);
                //visit(t);
                t.setPos(currentFunction.newReg());
                //f(t.type() instanceof ArrayType || t.type() instanceof ClassType) currentFunction.addIns(new Assign(t.pos(), new Imm(0)));
                s = PhiReg.getParameterReg(i);
                currentFunction.addIns(new Assign(t.pos(), s));
            } else {
                Entity t = entity.varList().get(i);
                t.setPos(new Mem(PhiReg.rbp, null, 0, 16 + (i - 6) * 8));
            }
        }
        if (entity.name().equals("main")) {
            for (DefinitionNode node : ast.definitionNodes()) if (node instanceof VariableDefinitionNode) visit((VariableDefinitionNode) node);

        }
        visit(entity.body());
        if (currentFunction.insList().size() == 0 || !(currentFunction.insList().get(currentFunction.insList().size() - 1) instanceof Jump)) {  // add return
            currentFunction.addIns(new Jump(end));
        }
        currentFunction.addIns(end);
    }

    @Override public void visit(VariableDefinitionNode node) {

        ExprNode init = node.entity().Expr();
        if (init != null) {
            //ExprStmtNode assign = new ExprStmtNode(node.location(), new AssignNode(new VariableNode(node.entity(), node.location()), init));
            //visit(assign);
            visitExpr(init);
        }
        VariableEntity t = node.entity();
        if (!t.isGlobal()) t.setPos(currentFunction.newReg());
        if (init != null) currentFunction.addIns(new Assign(t.pos(), init.operand()));
        else {
            if(t.type() instanceof ArrayType || t.type() instanceof ClassType) {
                currentFunction.addIns(new Assign(t.pos(), new Imm(0)));
            }
        }
    }

    public void visit(BlockNode node) {

        for (StmtNode stmt : node.stmts()) stmt.accept(this);
    }

    public void visit(AssignNode node) {

        visitExpr(node.lhs());
        visitExpr(node.rhs());
    //    if(node.lhs().operand() == null){
    //        int a = 1 + 1;
    //    }
        currentFunction.addIns(new Assign(node.lhs().operand(), node.rhs().operand()));
        node.setOperand(null);
    }

    @Override public void visit(IfNode node) {

        visitExpr(node.cond());
        Label thenLabel = new Label();
        Label elseLabel = new Label();
        Label endLabel  = new Label();
        currentFunction.addIns(new Cjump(node.cond().operand(), new Imm(1), Cjump.Type.GE, thenLabel));
        currentFunction.addIns(new Jump(elseLabel));
        currentFunction.addIns(thenLabel);
        if (node.thenBody() != null) visitStmt(node.thenBody());
        currentFunction.addIns(new Jump(endLabel));
        currentFunction.addIns(elseLabel);
        if (node.elseBody() != null) visitStmt(node.elseBody());
        currentFunction.addIns(endLabel);
    }

    private void visitLoop(ExprNode init, ExprNode cond, ExprNode incr, StmtNode body) {

        Label lastContinueLabel = continueLabel;
        Label lastBreakLabel = breakLabel;
        Label startLabel = new Label();
        Label trueLabel = new Label();
        continueLabel = new Label();
        breakLabel = new Label();
        if (init != null) visitExpr(init);
        currentFunction.addIns(startLabel);
        if (cond != null) {
            visitExpr(cond);
            currentFunction.addIns(new Cjump(cond.operand(), new Imm(1),Cjump.Type.GE, trueLabel));
            currentFunction.addIns(new Jump(breakLabel));
        }
        else currentFunction.addIns(new Jump(trueLabel));
        currentFunction.addIns(trueLabel);
        if (body != null) visitStmt(body);
        currentFunction.addIns(continueLabel);
        if (incr != null) visitExpr(incr);
        currentFunction.addIns(new Jump(startLabel));
        currentFunction.addIns(breakLabel);
        continueLabel = lastContinueLabel;
        breakLabel = lastBreakLabel;
    }

    @Override public void visit(WhileNode node) {

        visitLoop(null, node.cond(), null, node.body());
    }

    @Override public void visit(ForNode node) {

        visitLoop(node.init(), node.cond(), node.incr(), node.body());
    }

    @Override public void visit(ContinueNode node) {

        currentFunction.addIns(new Jump(continueLabel));
    }

    @Override public void visit(BreakNode node) {

        currentFunction.addIns(new Jump(breakLabel));
    }

    @Override public void visit(ReturnNode node) {

        if(node.expr() != null) {
            visitExpr(node.expr());
            currentFunction.addIns(new Assign(PhiReg.rax, node.expr().operand()));
        }
        currentFunction.addIns(new Jump(currentFunction.endLabel()));
    }

    @Override public void visit(ExprStmtNode node) {

        visitExpr(node.expr());
    }

    @Override public void visit(IntegerLiteralNode node) {

        node.setOperand(new Imm(node.value()));
    }

    @Override public void visit(StringLiteralNode node) {

        node.setOperand(Ir.add((StringConstantEntity)node.entity()));
    }

    @Override public void visit(BoolLiteralNode node) {

        node.setOperand(new Imm(node.value() ? 1 : 0));
    }


    @Override public void visit(BinaryOpNode node) {

        Binary.BinaryOp op;
        switch(node.operator()) {
            case ADD: op = ADD; break;
            case SUB: op = SUB; break;
            case MUL: op = MUL; break;
            case DIV: op = DIV; break;
            case MOD: op = MOD; break;
            case LSHIFT:  op = LSHIFT;  break;
            case RSHIFT:  op = RSHIFT;  break;
            case B_AND: op = B_AND; break;
            case B_XOR: op = B_XOR; break;
            case B_OR:  op = B_OR;  break;
            case L_AND: op = B_AND; break;
            case L_OR:  op = B_OR;  break;
            case GT: op = GT; break;
            case LT: op = LT; break;
            case GE: op = GE; break;
            case LE: op = LE; break;
            case EQ: op = EQ; break;
            case NE: op = NE; break;
            default: throw new Error();
        }
        node.setOperand(currentFunction.newReg());
        visitExpr(node.left());
        visitExpr(node.right());
        if (node.left().type().isString()) {
            List<Operand> args = new ArrayList<Operand>();
            args.add(node.left().operand());
            args.add(node.right().operand());
            switch(node.operator()) {
                case ADD : currentFunction.addIns(new Call(Str_ADD, args, node.operand())); break;
                case EQ : currentFunction.addIns(new Call(Str_EQ, args, node.operand())); break;
                case NE : currentFunction.addIns(new Call(Str_NE, args, node.operand())); break;
                case GT : currentFunction.addIns(new Call(Str_GT, args, node.operand())); break;
                case LT : currentFunction.addIns(new Call(Str_LT, args, node.operand())); break;
                case LE : currentFunction.addIns(new Call(Str_LE, args, node.operand())); break;
                case GE : currentFunction.addIns(new Call(Str_GE, args, node.operand())); break;
                default : throw new Error();
            }
        }
        else currentFunction.addIns(new Binary(node.operand(), op, node.left().operand(), node.right().operand()));
    }

    @Override public void visit(LogicalAndNode node) {
        node.setOperand(currentFunction.newReg());
        visitExpr(node.left());
        Label FaiLabel = new Label();
        Label OutLabel = new Label();
        currentFunction.addIns(new Cjump(node.left().operand(), new Imm(1), Cjump.Type.GE, FaiLabel));
        currentFunction.addIns(new Assign(node.operand(), new Imm(0)));
        currentFunction.addIns(new Jump(OutLabel));
        currentFunction.addIns(FaiLabel);
        visitExpr(node.right());
        currentFunction.addIns(new Assign(node.operand(), node.right().operand()));
        currentFunction.addIns(OutLabel);
    }

    @Override public void visit(LogicalOrNode node) {
        node.setOperand(currentFunction.newReg());
        visitExpr(node.left());
        Label SucLabel = new Label();
        Label OutLabel = new Label();
        currentFunction.addIns(new Cjump(node.left().operand(), new Imm(1), Cjump.Type.GE, SucLabel));
        visitExpr(node.right());
        currentFunction.addIns(new Assign(node.operand(), node.right().operand()));
        currentFunction.addIns(new Jump(OutLabel));
        currentFunction.addIns(SucLabel);
        currentFunction.addIns(new Assign(node.operand(), new Imm(1)));
        currentFunction.addIns(OutLabel);
    }


    @Override public void visit(PrefixOpNode node) {

        visitExpr(node.expr());
        node.setOperand(node.expr().operand());
        switch (node.operator()) {
            case PRE_DEC :
                currentFunction.addIns(new Binary(node.operand(), Binary.BinaryOp.SUB, node.operand(), new Imm(1)));
                break;
            case PRE_INC :
                currentFunction.addIns(new Binary(node.operand(), Binary.BinaryOp.ADD, node.operand(), new Imm(1)));
                break;
            default: {
                visit((UnaryOpNode)node);
                // System.out.println(node.operator());
                // throw new Error();
            }
        }
    }

    @Override public void visit(SuffixOpNode node) {

        visitExpr(node.expr());
        node.setOperand(currentFunction.newReg());
        currentFunction.addIns(new Assign(node.operand(), node.expr().operand()));
        switch (node.operator()) {
            case SUF_DEC :
                currentFunction.addIns(new Binary(node.expr().operand(), Binary.BinaryOp.SUB, node.expr().operand(), new Imm(1)));
                break;
            case SUF_INC :
                currentFunction.addIns(new Binary(node.expr().operand(), Binary.BinaryOp.ADD, node.expr().operand(), new Imm(1)));
                break;
            default: throw new Error();
        }
    }

    @Override public void visit(UnaryOpNode node) {

        Unary.UnaryOp op;

        switch (node.operator()) {

            case MINUS : op = Unary.UnaryOp.MINUS; break;
            case B_NOT : op = Unary.UnaryOp.B_NOT; break;
            case L_NOT : op = Unary.UnaryOp.L_NOT; break;
            default : throw new Error();
        }
        node.setOperand(currentFunction.newReg());
        currentFunction.addIns(new Assign(node.operand(), node.expr().operand()));
        currentFunction.addIns(new Unary(node.operand(), op));
    }

    @Override public void visit(FuncallNode node) {

        FunctionEntity entity = node.functionType().entity();
        node.setLabel(new Label(entity.name()));
        //currentFunction = entity;
        List<Operand> args = new ArrayList<>();
        for (ExprNode exprNode : node.varList()) {
            visitExpr(exprNode);
            args.add(exprNode.operand());
        }
        node.setOperand(currentFunction.newReg());
        Call call = new Call(entity, args, node.operand());
        currentFunction.addIns(call);
    }

    //void

    @Override public void visit(CreatorNode node) {
        if (node.type() instanceof ArrayType) node.setOperand(newArray(node, 0));
        else node.setOperand(newClass((ClassType)node.type()));

    }

    private Operand newArray(CreatorNode node, int now) { // 0

        visitExpr(node.exprs().get(now));
        VirReg d = currentFunction.newReg();
        VirReg s = currentFunction.newReg();
        currentFunction.addIns(new Assign(s, node.exprs().get(now).operand()));
        currentFunction.addIns(new Binary(s, ADD, s, new Imm(1)));
        currentFunction.addIns(new Binary(s, MUL, s, new Imm(8)));
        List<Operand> args = new ArrayList<Operand>();
        args.add(s);
        currentFunction.addIns(new Call(malloc, args, d));
        currentFunction.addIns(new Assign(new Mem(d, null, 0, 0), node.exprs().get(now).operand()));
        currentFunction.addIns(new Binary(d, ADD, d, new Imm(8)));
        if (node.exprs().size() > now + 1) {
            currentFunction.addIns(new Assign(s, node.exprs().get(now).operand()));
            Label creator = new Label();
            currentFunction.addIns(creator);
            currentFunction.addIns(new Binary(s, SUB, s, new Imm(1)));
            currentFunction.addIns(new Assign(new Mem(d, s, 8, 0), newArray(node, now + 1)));
            currentFunction.addIns(new Cjump(s, new Imm(0), Cjump.Type.NE, creator));
        }
        else if (node.exprs().size() == now + 1 && node.type() instanceof ClassType) {
            currentFunction.addIns(new Assign(s, node.exprs().get(now).operand()));
            Label creator = new Label();
            currentFunction.addIns(creator);
            currentFunction.addIns(new Binary(s, SUB, s, new Imm(1)));
            currentFunction.addIns(new Assign(new Mem(d, s, 8, 0), newClass((ClassType)node.type())));
            currentFunction.addIns(new Cjump(s, new Imm(0), Cjump.Type.NE, creator));
        }

        return d;
    }

    private Operand newClass(ClassType type) {

        VirReg d = currentFunction.newReg();
        ArrayList<Operand> args = new ArrayList<Operand>();
        args.add(new Imm(type.entity().size()));
        currentFunction.addIns(new Call(malloc, args, d));
        if(type.entity().constructor() != null) {
            ArrayList<Operand> _args = new ArrayList<Operand>();
            _args.add(d);
            currentFunction.addIns(new Call(type.entity().constructor(), _args, new Mem(d, null, 0, 0)));
        }
        return d;
    }

    private VirReg toReg(Operand r) {
        if(r instanceof Reg) return (VirReg)r;
        else {
            VirReg t = currentFunction.newReg();
            currentFunction.addIns(new Assign(t, r));
            return t;
        }
    }

    @Override public void visit(ArefNode node) {
        visitExpr(node.expr());
        visitExpr(node.index());
        Reg index = toReg(node.index().operand());
        Reg base = toReg(node.expr().operand());
        node.setOperand(new Mem(base, index, 8, 0));
    }

    @Override public void visit(MemberNode node) {
        visitExpr(node.expr());
        Reg base = toReg(node.expr().operand());
        node.setOperand(new Mem(base, null, 0, ((VariableEntity)node.entity()).Offset()));
    }

    public void print() {

        for (FunctionEntity entity : ast.functionEntities()) {
            System.out.println("\n" + entity.name() + "\n");
            for(Ins item : entity.insList()) System.out.print(item.toString());//ln }

        }

        for (ClassEntity entity : ast.classEntities()) {
            for (FunctionDefinitionNode node : entity.memberFuncs()) {
                //currentFunction = node.entity();
                System.out.println("\n" + node.entity().name() + " in Class:" + entity.name() + "\n");
                for(Ins item : node.entity().insList()) System.out.print(item.toString());
            }
        }

    }


    @Override public void visit(VariableNode node) {

        if (node.isMember()) {
            Reg base = toReg(node.getThisPointer().pos());
            int offset = ((VariableEntity)node.entity()).Offset();
            node.setOperand(new Mem(base, null, 0, offset));
        }
     /*   else if(node.isMember()) {
            node.setOperand(node.getThisPointer().pos());
        } && node.getThisPointer() instanceof  ClassEntity*/
        else node.setOperand((node.entity()).pos());
    }

    public IR Ir() {

        return Ir;
    }

}
