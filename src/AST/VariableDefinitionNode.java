package AST;

import Entity.VariableEntity;
import FrontEnd.ASTVisitor;

public class VariableDefinitionNode extends DefinitionNode {

    private VariableEntity entity;

    public VariableDefinitionNode(VariableEntity entity) {

    super(entity.location(), entity.name());
        this.entity = entity;
    }

    public VariableEntity entity() {

        return entity;
    }

    @Override
    public void accept(ASTVisitor visitor) {

        visitor.visit(this);
    }

}
