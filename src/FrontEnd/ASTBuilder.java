package FrontEnd;

import AST.DefinitionNode;
import Entity.*;
import AST.*;
import Type.*;
import Error.SemanticError;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.TerminalNode;
import parser.MsBaseListener;
import parser.MsParser;

import java.util.LinkedList;
import java.util.List;

public class ASTBuilder extends MsBaseListener {

    private AST ast;

    private ParseTreeProperty<Object> mp = new ParseTreeProperty<>();

    static String Constructor_Pre = "_Constructor";
    static public String StringLiteral_Pre = "_StringLiteral";
    //static public String Class_Pre = "_Class";

    private StmtNode getStmtNode(MsParser.StatementContext ctx) {
        if (ctx == null) return null;
        else return (StmtNode)mp.get(ctx);
    }

    private ExprNode getExprNode(MsParser.ExpressionContext ctx) {
        if (ctx == null) {
            //System.out.println(111);
            return null;
        }
        else return (ExprNode)mp.get(ctx);
    }

    public AST getAST() {
        return ast;
    }

    @Override public void exitCompilationUnit(MsParser.CompilationUnitContext ctx) {
        List<DefinitionNode> definitionNodes = new LinkedList<>();
        List<FunctionEntity> functionEntities = new LinkedList<>();
        List<ClassEntity> classEntities = new LinkedList<>();
        List<VariableEntity> variableEntities = new LinkedList<>();

        for (ParserRuleContext parserRuleContext : ctx.getRuleContexts(ParserRuleContext.class)) {
            DefinitionNode node = (DefinitionNode)mp.get(parserRuleContext);
            definitionNodes.add(node);
            if (node instanceof VariableDefinitionNode) variableEntities.add(((VariableDefinitionNode)node).entity());
            else if (node instanceof FunctionDefinitionNode) functionEntities.add(((FunctionDefinitionNode)node).entity());
            else if (node instanceof ClassDefinitionNode) classEntities.add(((ClassDefinitionNode)node).entity());
        }

        ast = new AST(definitionNodes, classEntities, functionEntities, variableEntities);
    }

    @Override public void exitVariableDefinition(MsParser.VariableDefinitionContext ctx) {
        VariableEntity entity = new VariableEntity(new Location(ctx.Identifier()), (Type)mp.get(ctx.typeType()), ctx.Identifier().getText(), getExprNode(ctx.expression()));
        VariableDefinitionNode varDefNode = new VariableDefinitionNode(entity);
        mp.put(ctx, varDefNode);
    }

    @Override public void exitFunctionDefinition(MsParser.FunctionDefinitionContext ctx) {
        List<Entity> varList = new LinkedList<>();
        for(MsParser.ParameterContext pctx : ctx.parameter()) varList.add((Entity)mp.get(pctx));
        FunctionEntity entity;
        if(ctx.ret == null) {
            entity = new FunctionEntity(new Location(ctx.name), new ClassType(ctx.name.getText()), Constructor_Pre + ctx.name.getText(), varList, (BlockNode) mp.get(ctx.block()));
            entity.setIsConstructor(true);
        }
        else entity = new FunctionEntity(new Location(ctx.name), (Type) mp.get(ctx.ret), ctx.name.getText(), varList, (BlockNode) mp.get(ctx.block()));
        mp.put(ctx, new FunctionDefinitionNode(entity));
    }

    @Override public void exitClassDefinition(MsParser.ClassDefinitionContext ctx) {
        String classname = ctx.name.getText();
        FunctionEntity constructor = null;
        List<FunctionDefinitionNode> fs = new LinkedList<>();
        List<VariableDefinitionNode> vs = new LinkedList<>();
        for (MsParser.VariableDefinitionContext vctx : ctx.variableDefinition()) vs.add((VariableDefinitionNode)mp.get(vctx));
        for (MsParser.FunctionDefinitionContext item : ctx.functionDefinition()) {
            FunctionDefinitionNode node = (FunctionDefinitionNode)mp.get(item);
            fs.add(node);
            FunctionEntity entity = node.entity();
            if (entity.isConstructor()) {
                constructor = entity;
                if (!entity.name().equals(Constructor_Pre + classname)) throw new SemanticError(new Location(ctx.name), "wrong constructor");
            }
        }
        ClassEntity entity = new ClassEntity(new Location(ctx.name), classname, vs, fs, constructor);
        mp.put(ctx, new ClassDefinitionNode(entity));
    }

    @Override public void exitBlock(MsParser.BlockContext ctx) {
        List<StmtNode> stmts = new LinkedList<>();
        for (MsParser.StatementContext sctx : ctx.statement()) {
            StmtNode stmt = getStmtNode(sctx);
            if (stmt != null) stmts.add(stmt);
        }
        BlockNode blocknode = new BlockNode(new Location(ctx), stmts);
        mp.put(ctx, blocknode);
    }

    @Override public void exitTypeType(MsParser.TypeTypeContext ctx) {
        Type type = (ctx.Identifier() == null) ? (Type)mp.get(ctx.primitiveType()) : new ClassType(ctx.Identifier().getText());
        int dimension = (ctx.getChildCount() - 1) / 2;
        if (dimension == 0) mp.put(ctx, type);
        else mp.put(ctx, new ArrayType(type, dimension));
    }

    @Override public void exitPrimitiveType(MsParser.PrimitiveTypeContext ctx) {
        Type type;
        switch (ctx.type.getText()) {
            case "int"    : type = new IntType();     break;
            case "void"   : type = new VoidType();    break;
            case "bool"   : type = new BoolType();    break;
            case "string" : type = new StringType();  break;
            default:throw new Error();
        }
        mp.put(ctx, type);
    }

    @Override public void exitParameter(MsParser.ParameterContext ctx) {
        Entity entity = new Entity(new Location(ctx), (Type)mp.get(ctx.typeType()), ctx.Identifier().getText());
        mp.put(ctx, entity);
    }

    @Override public void exitBlockStmt(MsParser.BlockStmtContext ctx) {
        mp.put(ctx, mp.get(ctx.block()));
    }

    @Override public void exitVarDefStmt(MsParser.VarDefStmtContext ctx) {
        mp.put(ctx, mp.get(ctx.variableDefinition()));
    }

    @Override public void exitIfStmt(MsParser.IfStmtContext ctx) {
        IfNode ifNode = new IfNode(new Location(ctx), getExprNode(ctx.expression()), getStmtNode(ctx.statement(0)), getStmtNode(ctx.statement(1)));
        mp.put(ctx, ifNode);
    }

    @Override public void exitForStmt(MsParser.ForStmtContext ctx) {
        ForNode forNode = new ForNode(new Location(ctx), getExprNode(ctx.init), getExprNode(ctx.cond), getExprNode(ctx.incr), getStmtNode(ctx.statement()));
        mp.put(ctx, forNode);
    }

    @Override public void exitWhileStmt(MsParser.WhileStmtContext ctx) {
        WhileNode whilenode = new WhileNode(new Location(ctx), getExprNode(ctx.expression()), getStmtNode(ctx.statement()));
        mp.put(ctx, whilenode);
    }

    @Override public void exitReturnStmt(MsParser.ReturnStmtContext ctx) {
        ReturnNode returnNode = new ReturnNode(new Location(ctx), getExprNode(ctx.expression()));
        mp.put(ctx, returnNode);
    }

    @Override public void exitBreakStmt(MsParser.BreakStmtContext ctx) {
        BreakNode breakNode = new BreakNode(new Location(ctx));
        mp.put(ctx, breakNode);
    }

    @Override public void exitContinueStmt(MsParser.ContinueStmtContext ctx) {
        ContinueNode continueNode = new ContinueNode(new Location(ctx));
        mp.put(ctx, continueNode);
    }

    @Override public void exitExprStmt(MsParser.ExprStmtContext ctx) {
        ExprStmtNode exprStmtNode = new ExprStmtNode(new Location(ctx), getExprNode(ctx.expression()));
        mp.put(ctx, exprStmtNode);
    }

    @Override public void exitBlankStmt(MsParser.BlankStmtContext ctx) {
        mp.put(ctx, null);
    }

    @Override public void enterExpressionList(MsParser.ExpressionListContext ctx) {
        List<ExprNode> exprs = new LinkedList<>();
        for (MsParser.ExpressionContext ectx : ctx.expression()) exprs.add(getExprNode(ectx));
        mp.put(ctx, exprs);
    }

    @Override public void exitExpressionList(MsParser.ExpressionListContext ctx) {
        List<ExprNode> exprs = new LinkedList<>();
        for (MsParser.ExpressionContext ectx : ctx.expression()) exprs.add(getExprNode(ectx));
        mp.put(ctx, exprs);
    }

    @Override public void exitNewExpr(MsParser.NewExprContext ctx) {
        mp.put(ctx, mp.get(ctx.creator()));
    }

    @Override public void exitLogicalOrExpr(MsParser.LogicalOrExprContext ctx) {
        LogicalOrNode logicalOrNode = new LogicalOrNode(getExprNode(ctx.expression(0)), getExprNode(ctx.expression(1)));
        mp.put(ctx, logicalOrNode);
    }


    @Override public void exitPrefixExpr(MsParser.PrefixExprContext ctx) {
        UnaryOpNode.UnaryOp op;
        switch (ctx.op.getText()) {
            case "~"  : op = UnaryOpNode.UnaryOp.B_NOT; break;
            case "!"  : op = UnaryOpNode.UnaryOp.L_NOT; break;
            case "+"  : op = UnaryOpNode.UnaryOp.ADD;   break;
            case "-"  : op = UnaryOpNode.UnaryOp.MINUS; break;
            case "++" : op = UnaryOpNode.UnaryOp.PRE_INC; break;
            case "--" : op = UnaryOpNode.UnaryOp.PRE_DEC; break;
            default: throw new Error();
        }
        mp.put(ctx, new PrefixOpNode(op, getExprNode(ctx.expression())));
    }

    @Override public void exitPrimaryExpr(MsParser.PrimaryExprContext ctx) {
        mp.put(ctx, mp.get(ctx.primary()));
    }

    @Override public void exitLogicalAndExpr(MsParser.LogicalAndExprContext ctx) {
        LogicalAndNode logicalAndNode = new LogicalAndNode(getExprNode(ctx.expression(0)), getExprNode(ctx.expression(1)));
        mp.put(ctx, logicalAndNode);
    }

    @Override public void exitFuncallExpr(MsParser.FuncallExprContext ctx) {
        List<ExprNode> varList;
        if (ctx.expressionList() == null) varList = new LinkedList<>();
        else varList = (List<ExprNode>)mp.get(ctx.expressionList());
        mp.put(ctx, new FuncallNode(getExprNode(ctx.expression()), varList));
    }

    @Override public void exitMemberExpr(MsParser.MemberExprContext ctx) {
        MemberNode memberNode = new MemberNode(getExprNode(ctx.expression()), ctx.Identifier().getText());
        mp.put(ctx, memberNode);
    }

    @Override public void exitArefExpr(MsParser.ArefExprContext ctx) {
        ArefNode arefNode = new ArefNode(getExprNode(ctx.expression(0)), getExprNode(ctx.expression(1)));
        mp.put(ctx, arefNode);
    }

    @Override public void exitSuffixExpr(MsParser.SuffixExprContext ctx) {
        UnaryOpNode.UnaryOp op;
        switch (ctx.op.getText()) {
            case "++" : op = UnaryOpNode.UnaryOp.SUF_INC; break;
            case "--" : op = UnaryOpNode.UnaryOp.SUF_DEC; break;
            default: throw new Error();
        }
        mp.put(ctx, new SuffixOpNode(op, getExprNode(ctx.expression())));
    }

    @Override public void exitBinaryExpr(MsParser.BinaryExprContext ctx) {
        BinaryOpNode.BinaryOp op;
        switch (ctx.op.getText()) {
            case "&"  : op = BinaryOpNode.BinaryOp.B_AND; break;
            case "^"  : op = BinaryOpNode.BinaryOp.B_XOR; break;
            case "|"  : op = BinaryOpNode.BinaryOp.B_OR; break;
            case "*"  : op = BinaryOpNode.BinaryOp.MUL; break;
            case "+"  : op = BinaryOpNode.BinaryOp.ADD; break;
            case "-"  : op = BinaryOpNode.BinaryOp.SUB;  break;
            case "/"  : op = BinaryOpNode.BinaryOp.DIV; break;
            case "%"  : op = BinaryOpNode.BinaryOp.MOD; break;
            case ">=" : op = BinaryOpNode.BinaryOp.GE; break;
            case "<=" : op = BinaryOpNode.BinaryOp.LE; break;
            case "==" : op = BinaryOpNode.BinaryOp.EQ; break;
            case "!=" : op = BinaryOpNode.BinaryOp.NE; break;
            case "<<" : op = BinaryOpNode.BinaryOp.LSHIFT; break;
            case ">>" : op = BinaryOpNode.BinaryOp.RSHIFT; break;
            case ">"  : op = BinaryOpNode.BinaryOp.GT; break;
            case "<"  : op = BinaryOpNode.BinaryOp.LT; break;
            default   : throw new Error();
        }
        BinaryOpNode binaryOpNode = new BinaryOpNode(op, getExprNode(ctx.expression(0)), getExprNode(ctx.expression(1)));
        mp.put(ctx, binaryOpNode);

    }

    @Override public void exitAssignExpr(MsParser.AssignExprContext ctx) {
        AssignNode assignNode = new AssignNode(getExprNode(ctx.expression(0)), getExprNode(ctx.expression(1)));
        mp.put(ctx, assignNode);
    }

    @Override public void exitSubExpr(MsParser.SubExprContext ctx) {
        mp.put(ctx, mp.get(ctx.expression()));
    }

    @Override public void exitThisExpr(MsParser.ThisExprContext ctx) {
        VariableNode variableNode = new VariableNode(new Location(ctx), "this");
        mp.put(ctx, variableNode);
    }

    @Override public void exitVariableExpr(MsParser.VariableExprContext ctx) {
        VariableNode variableNode = new VariableNode(new Location(ctx.Identifier()), ctx.Identifier().getText());
        mp.put(ctx, variableNode);
    }

    @Override public void exitLiteralExpr(MsParser.LiteralExprContext ctx) {
        mp.put(ctx, mp.get(ctx.literal()));
    }

    @Override public void exitStringConst(MsParser.StringConstContext ctx) {
        String s = ctx.StringLiteral().getText();
        s = s.substring(1, s.length()-1); //""
        StringLiteralNode stringLiteralNode = new StringLiteralNode(new Location(ctx), StringLiteral_Pre + s);
        mp.put(ctx, stringLiteralNode);
    }

    @Override public void exitBoolConst(MsParser.BoolConstContext ctx) {
        BoolLiteralNode boolLiteralNode = new BoolLiteralNode(new Location(ctx), ctx.value.getText().equals("true"));
        mp.put(ctx, boolLiteralNode);
    }

    @Override public void exitNullConst(MsParser.NullConstContext ctx) {
        VariableNode variableNode = new VariableNode(new Location(ctx), "null");
        mp.put(ctx, variableNode);
    }

    @Override public void exitErrorCreator(MsParser.ErrorCreatorContext ctx) {
        throw new SemanticError(new Location(ctx), "wrong creator");
    }

    @Override public void exitArrayCreator(MsParser.ArrayCreatorContext ctx) {
        Type Type = (ctx.Identifier() == null) ? (Type)mp.get(ctx.primitiveType()) : new ClassType(ctx.Identifier().getText());
        List<MsParser.ExpressionContext> exprs = ctx.expression();
        int dimension = (ctx.getChildCount() - 1 - exprs.size()) / 2;
        Type type = new ArrayType(Type, dimension);
        List<ExprNode> exprNodes = new LinkedList<>();
        for (MsParser.ExpressionContext ectx : exprs) exprNodes.add(getExprNode(ectx));
        mp.put(ctx, new CreatorNode(new Location(ctx), type, exprNodes));
    }

    @Override public void exitNonarrayCreator(MsParser.NonarrayCreatorContext ctx) {
        Type type = new ClassType(ctx.Identifier().getText());
        CreatorNode creatorNode = new CreatorNode(new Location(ctx), type, null);
        mp.put(ctx, creatorNode);
    }

    @Override
    public void exitDecIntegerConst(MsParser.DecIntegerConstContext ctx) {
        IntegerLiteralNode integerLiteralNode =  new IntegerLiteralNode(new Location(ctx), Long.parseLong(ctx.DecimalInteger().getText()));
        mp.put(ctx, integerLiteralNode);
    }

}
