package IR;

import AST.DefinitionNode;
import Entity.*;
import IR.Operand.GlobalAddr;
//import Entity.FunctionEntity;
//import Entity.VariableEntity;

import java.util.ArrayList;
import java.util.List;

public class IR {

    //private List<DefinitionNode> definitionNodes;
    //private List<ClassEntity> classEntities;
    //private List<FunctionEntity> functionEntities;
    //private List<VariableEntity> variableEntities;

    //public List<Function> functions = new ArrayList<>();
    //public List<VarDec> globalVariables = new ArrayList<>();
    public List<StringConstantEntity> stringConstants = new ArrayList<>();

    public GlobalAddr add(StringConstantEntity t) {
        stringConstants.add(t);
        return new GlobalAddr("__string" + Integer.toString(stringConstants.size() - 1), true);
    }

}
