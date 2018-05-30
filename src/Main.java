import FrontEnd.ASTBuilder;
import FrontEnd.SyntaxErrorListener;
import FrontEnd.AST;
import Error.SemanticError;
import backend.Allocator;
import backend.IRBuilder;
import backend.LivenessAnalyzer;
import backend.Translator;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import parser.MsLexer;
import parser.MsParser;

import java.io.*;

public class Main {

    public static void main(String[] args) throws Exception {

        File file = new File("program.txt");
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
            ast.BulidScope();
            ast.checkSemantic();
            IRBuilder irBuilder = new IRBuilder(ast);
            irBuilder.generateIR();
            Allocator allocator = new Allocator();
            allocator.allocate(irBuilder.Ir());
            //irBuilder.print();
            Translator translator = new Translator();
            FileOutputStream fout = new FileOutputStream(new File("result.nasm"));
            PrintWriter out = new PrintWriter(fout);
            out.println(translator.Translate(irBuilder.Ir()));
            out.close();
            //System.out.print(translator.Translate(irBuilder.Ir()));
            //ProgramAST.print();
        }
        catch (SemanticError e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

}
