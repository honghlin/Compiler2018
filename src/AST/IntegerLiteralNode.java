package AST;

import Type.IntType;

public class IntegerLiteralNode extends LiteralNode{

    private long value;

    public IntegerLiteralNode(Location loc, long value) {
        super(loc, new IntType());
        this.value = value;
    }

    //@Override
    //public <S,E> E accept(ASTVisitor<S,E> visitor) {

    //    return visitor.visit(this);
    //}

}
