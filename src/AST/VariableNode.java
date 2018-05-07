package AST;

import Entity.Entity;
import FrontEnd.ASTVisitor;
import Type.Type;

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
}
