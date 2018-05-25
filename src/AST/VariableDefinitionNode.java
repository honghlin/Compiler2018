package AST;

import Entity.Entity;
import Entity.VariableEntity;
import FrontEnd.ASTVisitor;
import IR.Operand.Operand;

import java.util.HashMap;

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

    @Override public VariableDefinitionNode Inline(HashMap<Entity, Operand> inlineMap) {

        VariableEntity t = new VariableEntity(entity);
        t.setName("inline__" + t.name());
        if(entity.Expr() != null) t.setExpr(entity.Expr().Inline(inlineMap));
        VariableDefinitionNode node = new VariableDefinitionNode(t);
        return node;
    }

}
