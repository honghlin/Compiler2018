package FrontEnd;

import Error.SemanticError;
import AST.Location;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class SyntaxErrorListener extends BaseErrorListener { //

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object symbol, int row, int column, String message, RecognitionException e) {
        throw new SemanticError(new Location(row, column), message);
    }

}
