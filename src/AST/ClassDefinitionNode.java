package AST;

import Entity.ClassEntity;
import Entity.Entity;
import FrontEnd.ASTVisitor;
import IR.Operand.Operand;

import java.util.HashMap;

public class ClassDefinitionNode extends DefinitionNode {

    private ClassEntity entity;

    public ClassDefinitionNode (ClassEntity entity) {

        super(entity.location(), entity.name());
        this.entity = entity;
    }

    public ClassEntity entity() {

        return entity;
    }

    @Override public void accept(ASTVisitor visitor) {

        visitor.visit(this);
    }

    @Override public ClassDefinitionNode Inline(HashMap<Entity, Operand> inlineMap) {

        return this;
    }

    @Override public ClassDefinitionNode copy() {

        ClassDefinitionNode node = new ClassDefinitionNode(entity);
        return node;
    }

}
