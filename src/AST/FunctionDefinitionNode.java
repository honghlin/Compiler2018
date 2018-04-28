package AST;

import Entity.FunctionEntity;

public class FunctionDefinitionNode extends DefinitionNode {

    private FunctionEntity entity;

    public FunctionDefinitionNode(FunctionEntity entity) {

        super(entity.location(), entity.name());
        this.entity = entity;
    }

    public FunctionEntity entity() {

        return entity;
    }

    //@Override
    //public <S,E> S accept(ASTVisitor<S,E> visitor) {

    //    return visitor.visit(this);
    //}

}
