package AST;

import Entity.Entity;
import FrontEnd.ASTVisitor;
import IR.Operand.Operand;
import Type.Type;
import Entity.VariableEntity;

import java.util.HashMap;

public class VariableNode extends LHSNode {

    private Location location;
    private String name;
    private Entity entity;
    private Entity thisPointer = null;

    public VariableNode(Location loc, String name) {

        this.location = loc;
        this.name = name;
    }

    public VariableNode(Entity entity) {

        this.entity = entity;
        this.name = entity.name();
    }

    public VariableNode(Entity entity, Location loc) {

        this.entity = entity;
        this.location = loc;
        this.name = entity.name();
    }

    public String name() {

        return name;
    }

    public Entity entity() {

        return entity;
    }

    public void setEntity(Entity entity) {

        this.entity = entity;
    }

    public void setThisPointer(Entity entity) {

        this.thisPointer = entity;
    }

    public Entity getThisPointer() {

        return thisPointer;
    }

    public boolean isMember() {

        return thisPointer != null;
    }

    @Override
    public Type type() {

        return entity.type();
    }

    @Override
    public Location location() {

        return location;
    }

    @Override
    public void accept(ASTVisitor visitor) {

        visitor.visit(this);
    }

    public VariableNode Inline(HashMap<Entity, Operand> inlineMap){

        VariableNode node = new VariableNode(entity);
        node.entity = new Entity(entity.location(), entity.type(), entity.name());
        if(inlineMap.containsKey(entity)) {
            node.entity.setPos(inlineMap.get(entity));
            node.setOperand(inlineMap.get(entity));
            return node;
        }
        if(isMember() && inlineMap.containsKey(thisPointer)) {
            thisPointer.setPos(inlineMap.get(thisPointer));// ((VariableEntity)) thisPointer != null &&
        }
        return this;
    }

}
