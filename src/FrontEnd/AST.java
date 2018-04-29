package FrontEnd;

import Entity.ClassEntity;
import Entity.FunctionEntity;
import Entity.VariableEntity;
import AST.DefinitionNode;
import Scope.Scope;

import java.util.List;

public class AST {

    private Scope scope;

    private List<DefinitionNode> definitionNodes;
    private List<ClassEntity> classEntities;
    private List<FunctionEntity> functionEntities;
    private List<VariableEntity> variableEntities;

    public AST(List<DefinitionNode> definitionNodes, List<ClassEntity> definedClass, List<FunctionEntity> definedFunction, List<VariableEntity> definedVariable) {

        super();
        this.definitionNodes  = definitionNodes;
        this.classEntities    = definedClass;
        this.functionEntities = definedFunction;
        this.variableEntities = definedVariable;
        this.scope = new Scope(true);
    }

    public void Init() {

        LoadLibrary LibF = new LoadLibrary();
        for(FunctionEntity entity : LoadLibrary.LibFunc()) scope.insert(entity);
        for (ClassEntity entity : classEntities) scope.insert(entity);
        for (FunctionEntity entity : functionEntities) scope.insert(entity);
        Initializer resolver = new Initializer(scope);
        for (DefinitionNode d : definitionNodes) resolver.visitDefinition(d);
    }

}
