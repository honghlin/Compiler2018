package AST;

import Entity.FunctionEntity;
import FrontEnd.ASTVisitor;

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

}
