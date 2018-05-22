package IR;

import Entity.FunctionEntity;

public class Funcall extends Ins {

    private FunctionEntity entity;
    int size;

    public Funcall(FunctionEntity entity, int size) {
        this.entity = entity;
        this.size = size;
    }

    public FunctionEntity entity() {
        return entity;
    }

    public int size() {
        return size;
    }

    @Override public String toString() {

        String t = "Call" + entity.name() + "(" + Integer.toString(size) + ")\n";
        return t;
    }

}
