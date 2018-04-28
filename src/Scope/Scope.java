package Scope;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Entity.Entity;
import Error.SemanticError;

public class Scope {

    private Map<String, Entity> entities = new HashMap<>();
    private List<Scope> children = new ArrayList<>();

    private Scope parent;
    private boolean isGlobal;

    public Scope(boolean isGlobal) {

        this.isGlobal = isGlobal;
    }

    public Scope(Scope parent) {

        this.parent = parent;
        this.isGlobal = (parent == null);
        if (this.parent != null) this.parent.addChildren(this);

    }

    public void insert(Entity entity) {

        if (entities.containsKey(entity.name())) throw new SemanticError(entity.location(),  entity.name());
        entities.put(entity.name(), entity);
    }


    public Entity search(String name) {

        Entity entity = entities.get(name);
        if (entity == null) return isGlobal ? null : parent.search(name);
        else return entity;
    }


    public Entity searchCurrently(String name) {

        return entities.get(name);
    }


    public Map<String, Entity> entities() {

        return entities;
    }

    public List<Scope> children() {

        return children;
    }

    public void addChildren(Scope s) {

        children.add(s);
    }

    public Scope parent() {

        return isGlobal ? null : parent;
    }

    public boolean isTopLevel() {

        return isGlobal;
    }


}
