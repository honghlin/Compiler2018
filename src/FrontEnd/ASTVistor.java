package FrontEnd;

import AST.*;

public interface ASTVistor {

    void visit(ClassDefinitionNode node);
    void visit(FunctionDefinitionNode node);
    void visit(VariableDefinitionNode node);

    void visit(AssignNode node);
    void visit(ArefNode node);
    void visit(BinaryOpNode node);
    void visit(BoolLiteralNode node);
    void visit(CreatorNode node);
    void visit(FuncallNode node);
    void visit(IntegerLiteralNode node);
    void visit(LogicalAndNode node);
    void visit(LogicalOrNode node);
    void visit(MemberNode node);
    void visit(PrefixOpNode node);
    void visit(StringLiteralNode node);
    void visit(SuffixOpNode node);
    void visit(UnaryOpNode node);
    void visit(VariableNode node);

    void visit(BlockNode node);
    void visit(BreakNode node);
    void visit(ContinueNode node);
    void visit(ExprStmtNode node);
    void visit(ForNode node);
    void visit(IfNode node);
    void visit(ReturnNode node);
    void visit(WhileNode node);

}
