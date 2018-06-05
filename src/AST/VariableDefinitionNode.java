package AST;

import Entity.Entity;
import Entity.VariableEntity;
import FrontEnd.ASTVisitor;
import IR.Operand.Operand;

import java.util.HashMap;

public class VariableDefinitionNode extends DefinitionNode {

    public VariableEntity entity; // private

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

    @Override public VariableDefinitionNode copy() {

        //VariableEntity t = new VariableEntity(entity);
        //t.setName("inline__" + t.name());
        //if(entity.Expr() != null) t.setExpr(entity.Expr().Inline(inlineMap));
        VariableDefinitionNode node = new VariableDefinitionNode(entity); // t
        return node;
    }

}
