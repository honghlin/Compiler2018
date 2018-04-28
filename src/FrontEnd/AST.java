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

}
