package AST;

import Entity.Entity;
import Entity.FunctionEntity;
import FrontEnd.ASTVisitor;
import IR.Operand.Operand;

import java.util.HashMap;

public class FunctionDefinitionNode extends DefinitionNode {

    private FunctionEntity entity;

    public FunctionDefinitionNode(FunctionEntity entity) {

        super(entity.location(), entity.name());
        this.entity = entity;
    }

    public FunctionEntity entity() {

        return entity;
    }

    @Override
    public void accept(ASTVisitor visitor) {

        visitor.visit(this);
    }

    @Override public FunctionDefinitionNode Inline(HashMap<Entity, Operand> inlineMap) {

        return null;
    }

}
