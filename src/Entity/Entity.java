package Entity;

import AST.Location;
import IR.Operand.Operand;
import Type.Type;

public class Entity {

    protected Location location;
    protected String name;
    protected Type type;
    private Operand pos;

    public Entity(Location loc, Type type, String name) {

        this.location = loc;
        this.type = type;
        this.name = name;
    }

    public String name() {

        return name;
    }

    public Type type() {

        return type;
    }

    public Location location() {

        return location;
    }

    public void setPos(Operand pos) {//private

        this.pos = pos;
    }

    public Operand pos() {

        return pos;
    }

    public void setName(String name) {

        this.name = name;
    }

}
