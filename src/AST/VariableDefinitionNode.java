package AST;

import Entity.VariableEntity;

public class VariableDefinitionNode extends DefinitionNode {

    private VariableEntity entity;

    public VariableDefinitionNode(VariableEntity entity) {

    super(entity.location(), entity.name());
        this.entity = entity;
    }

    public VariableEntity entity() {

        return entity;
    }

    //@Override
    //public <S,E> S accept(ASTVisitor<S,E> visitor) {

    //    return visitor.visit(this);
    //}

}
