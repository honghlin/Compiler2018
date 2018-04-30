import FrontEnd.ASTBuilder;
import FrontEnd.SyntaxErrorListener;
import FrontEnd.AST;
import Error.SemanticError;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import parser.MsLexer;
import parser.MsParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class Main {

    public static void main(String[] args) throws Exception {

        File file = new File("C:\\Users\\80780\\Desktop\\testcase\\test17 x.mx");
        InputStream in = new FileInputStream(file);
        getAST(in);

    }

    public static void getAST(InputStream in) throws Exception {

        ANTLRInputStream input = new ANTLRInputStream(in);
        MsLexer lexer = new MsLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MsParser parser = new MsParser(tokens);
        try {
            parser.removeErrorListeners();
            parser.addErrorListener(new SyntaxErrorListener());
            ParseTree tree = parser.compilationUnit();
            ParseTreeWalker walker = new ParseTreeWalker();
            ASTBuilder listener = new ASTBuilder();

            walker.walk(listener, tree);
            AST ast = listener.getAST();
            ast.Init();
            ast.checkSemantic();
            //ProgramAST.print();
        }
        catch (SemanticError e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

}
