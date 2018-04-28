package AST;

import Entity.ClassEntity;
import FrontEnd.ASTVisitor;

public class ClassDefinitionNode extends DefinitionNode {

    private ClassEntity entity;

    public ClassDefinitionNode (ClassEntity entity) {

        super(entity.location(), entity.name());
        this.entity = entity;
    }

    public ClassEntity entity() {

        return entity;
    }

    @Override
    public void accept(ASTVisitor visitor) {

        visitor.visit(this);
    }

}
