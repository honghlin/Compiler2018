package Type;

import Entity.ClassEntity;

public class ClassType extends Type {

    protected String name;

    protected ClassEntity entity;

    public ClassType(String name) {

        this.name = name;
    }

    public String name() {

        return name;
    }

    public ClassEntity entity() {

        return entity;
    }

    public void setEntity(ClassEntity entity) {

        this.entity = entity;
    }

    @Override
    public boolean isClass() {

        return true;
    }

    @Override
    public boolean isCompatibleWith(Type other) {
        if (other.isNull())    return true;
        if (!other.isClass())  return false;
        return entity.equals(((ClassType)other).entity);
    }

}
