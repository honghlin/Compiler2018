package FrontEnd;
import AST.Location;
import Type.*;
import Error.SemanticError;

public class TypeChecker {

    public static void checkType(Location loc, Type A, Type B) {

        if (!A.isCompatibleWith(B)) throw new SemanticError(loc, "Type Compatible Error");

    }

}
