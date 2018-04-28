package AST;

import Entity.ClassEntity;

public class ClassDefinitionNode extends DefinitionNode {

    private ClassEntity entity;

    public ClassDefinitionNode (ClassEntity entity) {

        super(entity.location(), entity.name());
        this.entity = entity;
    }

    public ClassEntity entity() {

        return entity;
    }

    //@Override
    //public <S,E> S accept(ASTVisitor<S,E> visitor) {

    //    return visitor.visit(this);
    //}

}
