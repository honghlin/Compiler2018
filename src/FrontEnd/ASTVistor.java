package FrontEnd;

import AST.*;

public interface ASTVistor<S, E> {

    S visit(ClassDefinitionNode node);
    S visit(FunctionDefinitionNode node);
    S visit(VariableDefinitionNode node);

    E visit(AssignNode node);
    E visit(ArefNode node);
    E visit(BinaryOpNode node);
    E visit(BoolLiteralNode node);
    E visit(CreatorNode node);
    E visit(FuncallNode node);
    E visit(IntegerLiteralNode node);
    E visit(LogicalAndNode node);
    E visit(LogicalOrNode node);
    E visit(MemberNode node);
    E visit(PrefixOpNode node);
    E visit(StringLiteralNode node);
    E visit(SuffixOpNode node);
    E visit(UnaryOpNode node);
    E visit(VariableNode node);

    S visit(BlockNode node);
    S visit(BreakNode node);
    S visit(ContinueNode node);
    S visit(ExprStmtNode node);
    S visit(ForNode node);
    S visit(IfNode node);
    S visit(ReturnNode node);
    S visit(WhileNode node);

}
