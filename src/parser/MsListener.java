// Generated from C:/Users/80780/Compiler2018/src/parser\Ms.g4 by ANTLR 4.7
package parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link MsParser}.
 */
public interface MsListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link MsParser#compilationUnit}.
	 * @param ctx the parse tree
	 */
	void enterCompilationUnit(MsParser.CompilationUnitContext ctx);
	/**
	 * Exit a parse tree produced by {@link MsParser#compilationUnit}.
	 * @param ctx the parse tree
	 */
	void exitCompilationUnit(MsParser.CompilationUnitContext ctx);
	/**
	 * Enter a parse tree produced by {@link MsParser#variableDefinition}.
	 * @param ctx the parse tree
	 */
	void enterVariableDefinition(MsParser.VariableDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MsParser#variableDefinition}.
	 * @param ctx the parse tree
	 */
	void exitVariableDefinition(MsParser.VariableDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MsParser#functionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterFunctionDefinition(MsParser.FunctionDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MsParser#functionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitFunctionDefinition(MsParser.FunctionDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MsParser#classDefinition}.
	 * @param ctx the parse tree
	 */
	void enterClassDefinition(MsParser.ClassDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MsParser#classDefinition}.
	 * @param ctx the parse tree
	 */
	void exitClassDefinition(MsParser.ClassDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MsParser#parameter}.
	 * @param ctx the parse tree
	 */
	void enterParameter(MsParser.ParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link MsParser#parameter}.
	 * @param ctx the parse tree
	 */
	void exitParameter(MsParser.ParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link MsParser#primitiveType}.
	 * @param ctx the parse tree
	 */
	void enterPrimitiveType(MsParser.PrimitiveTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MsParser#primitiveType}.
	 * @param ctx the parse tree
	 */
	void exitPrimitiveType(MsParser.PrimitiveTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MsParser#typeType}.
	 * @param ctx the parse tree
	 */
	void enterTypeType(MsParser.TypeTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MsParser#typeType}.
	 * @param ctx the parse tree
	 */
	void exitTypeType(MsParser.TypeTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MsParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(MsParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link MsParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(MsParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by the {@code blockStmt}
	 * labeled alternative in {@link MsParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterBlockStmt(MsParser.BlockStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code blockStmt}
	 * labeled alternative in {@link MsParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitBlockStmt(MsParser.BlockStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code varDefStmt}
	 * labeled alternative in {@link MsParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterVarDefStmt(MsParser.VarDefStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code varDefStmt}
	 * labeled alternative in {@link MsParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitVarDefStmt(MsParser.VarDefStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ifStmt}
	 * labeled alternative in {@link MsParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterIfStmt(MsParser.IfStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ifStmt}
	 * labeled alternative in {@link MsParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitIfStmt(MsParser.IfStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code forStmt}
	 * labeled alternative in {@link MsParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterForStmt(MsParser.ForStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code forStmt}
	 * labeled alternative in {@link MsParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitForStmt(MsParser.ForStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code whileStmt}
	 * labeled alternative in {@link MsParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterWhileStmt(MsParser.WhileStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code whileStmt}
	 * labeled alternative in {@link MsParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitWhileStmt(MsParser.WhileStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code returnStmt}
	 * labeled alternative in {@link MsParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterReturnStmt(MsParser.ReturnStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code returnStmt}
	 * labeled alternative in {@link MsParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitReturnStmt(MsParser.ReturnStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code breakStmt}
	 * labeled alternative in {@link MsParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterBreakStmt(MsParser.BreakStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code breakStmt}
	 * labeled alternative in {@link MsParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitBreakStmt(MsParser.BreakStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code continueStmt}
	 * labeled alternative in {@link MsParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterContinueStmt(MsParser.ContinueStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code continueStmt}
	 * labeled alternative in {@link MsParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitContinueStmt(MsParser.ContinueStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exprStmt}
	 * labeled alternative in {@link MsParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterExprStmt(MsParser.ExprStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exprStmt}
	 * labeled alternative in {@link MsParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitExprStmt(MsParser.ExprStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code blankStmt}
	 * labeled alternative in {@link MsParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterBlankStmt(MsParser.BlankStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code blankStmt}
	 * labeled alternative in {@link MsParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitBlankStmt(MsParser.BlankStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link MsParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void enterExpressionList(MsParser.ExpressionListContext ctx);
	/**
	 * Exit a parse tree produced by {@link MsParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void exitExpressionList(MsParser.ExpressionListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code newExpr}
	 * labeled alternative in {@link MsParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNewExpr(MsParser.NewExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code newExpr}
	 * labeled alternative in {@link MsParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNewExpr(MsParser.NewExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code logicalOrExpr}
	 * labeled alternative in {@link MsParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterLogicalOrExpr(MsParser.LogicalOrExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code logicalOrExpr}
	 * labeled alternative in {@link MsParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitLogicalOrExpr(MsParser.LogicalOrExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code prefixExpr}
	 * labeled alternative in {@link MsParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterPrefixExpr(MsParser.PrefixExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code prefixExpr}
	 * labeled alternative in {@link MsParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitPrefixExpr(MsParser.PrefixExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code primaryExpr}
	 * labeled alternative in {@link MsParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterPrimaryExpr(MsParser.PrimaryExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code primaryExpr}
	 * labeled alternative in {@link MsParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitPrimaryExpr(MsParser.PrimaryExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code logicalAndExpr}
	 * labeled alternative in {@link MsParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterLogicalAndExpr(MsParser.LogicalAndExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code logicalAndExpr}
	 * labeled alternative in {@link MsParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitLogicalAndExpr(MsParser.LogicalAndExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code funcallExpr}
	 * labeled alternative in {@link MsParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterFuncallExpr(MsParser.FuncallExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code funcallExpr}
	 * labeled alternative in {@link MsParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitFuncallExpr(MsParser.FuncallExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code memberExpr}
	 * labeled alternative in {@link MsParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterMemberExpr(MsParser.MemberExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code memberExpr}
	 * labeled alternative in {@link MsParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitMemberExpr(MsParser.MemberExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arefExpr}
	 * labeled alternative in {@link MsParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterArefExpr(MsParser.ArefExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arefExpr}
	 * labeled alternative in {@link MsParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitArefExpr(MsParser.ArefExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code suffixExpr}
	 * labeled alternative in {@link MsParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterSuffixExpr(MsParser.SuffixExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code suffixExpr}
	 * labeled alternative in {@link MsParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitSuffixExpr(MsParser.SuffixExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code binaryExpr}
	 * labeled alternative in {@link MsParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterBinaryExpr(MsParser.BinaryExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code binaryExpr}
	 * labeled alternative in {@link MsParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitBinaryExpr(MsParser.BinaryExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code assignExpr}
	 * labeled alternative in {@link MsParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterAssignExpr(MsParser.AssignExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code assignExpr}
	 * labeled alternative in {@link MsParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitAssignExpr(MsParser.AssignExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code subExpr}
	 * labeled alternative in {@link MsParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterSubExpr(MsParser.SubExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code subExpr}
	 * labeled alternative in {@link MsParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitSubExpr(MsParser.SubExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code thisExpr}
	 * labeled alternative in {@link MsParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterThisExpr(MsParser.ThisExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code thisExpr}
	 * labeled alternative in {@link MsParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitThisExpr(MsParser.ThisExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code variableExpr}
	 * labeled alternative in {@link MsParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterVariableExpr(MsParser.VariableExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code variableExpr}
	 * labeled alternative in {@link MsParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitVariableExpr(MsParser.VariableExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code literalExpr}
	 * labeled alternative in {@link MsParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterLiteralExpr(MsParser.LiteralExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code literalExpr}
	 * labeled alternative in {@link MsParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitLiteralExpr(MsParser.LiteralExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DecIntegerConst}
	 * labeled alternative in {@link MsParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterDecIntegerConst(MsParser.DecIntegerConstContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DecIntegerConst}
	 * labeled alternative in {@link MsParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitDecIntegerConst(MsParser.DecIntegerConstContext ctx);
	/**
	 * Enter a parse tree produced by the {@code StringConst}
	 * labeled alternative in {@link MsParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterStringConst(MsParser.StringConstContext ctx);
	/**
	 * Exit a parse tree produced by the {@code StringConst}
	 * labeled alternative in {@link MsParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitStringConst(MsParser.StringConstContext ctx);
	/**
	 * Enter a parse tree produced by the {@code boolConst}
	 * labeled alternative in {@link MsParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterBoolConst(MsParser.BoolConstContext ctx);
	/**
	 * Exit a parse tree produced by the {@code boolConst}
	 * labeled alternative in {@link MsParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitBoolConst(MsParser.BoolConstContext ctx);
	/**
	 * Enter a parse tree produced by the {@code nullConst}
	 * labeled alternative in {@link MsParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterNullConst(MsParser.NullConstContext ctx);
	/**
	 * Exit a parse tree produced by the {@code nullConst}
	 * labeled alternative in {@link MsParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitNullConst(MsParser.NullConstContext ctx);
	/**
	 * Enter a parse tree produced by the {@code errorCreator}
	 * labeled alternative in {@link MsParser#creator}.
	 * @param ctx the parse tree
	 */
	void enterErrorCreator(MsParser.ErrorCreatorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code errorCreator}
	 * labeled alternative in {@link MsParser#creator}.
	 * @param ctx the parse tree
	 */
	void exitErrorCreator(MsParser.ErrorCreatorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arrayCreator}
	 * labeled alternative in {@link MsParser#creator}.
	 * @param ctx the parse tree
	 */
	void enterArrayCreator(MsParser.ArrayCreatorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arrayCreator}
	 * labeled alternative in {@link MsParser#creator}.
	 * @param ctx the parse tree
	 */
	void exitArrayCreator(MsParser.ArrayCreatorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code nonarrayCreator}
	 * labeled alternative in {@link MsParser#creator}.
	 * @param ctx the parse tree
	 */
	void enterNonarrayCreator(MsParser.NonarrayCreatorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code nonarrayCreator}
	 * labeled alternative in {@link MsParser#creator}.
	 * @param ctx the parse tree
	 */
	void exitNonarrayCreator(MsParser.NonarrayCreatorContext ctx);
}