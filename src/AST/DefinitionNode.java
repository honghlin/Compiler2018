package AST;

abstract public class DefinitionNode extends StmtNode {

    protected String name;

    public DefinitionNode(Location loc, String name) {

        super(loc);
        this.name = name;
    }

    public String name() {

        return name;
    }

    //abstract public <S,E> S accept(ASTVisitor<S,E> visitor);

}
