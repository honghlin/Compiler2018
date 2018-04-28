package Error;

import AST.Location;

public class SemanticError extends Error {

    public SemanticError(Location loc, String message) {

        super(loc.toString() + message);
    }
}
