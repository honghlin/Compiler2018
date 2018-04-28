package AST;

import java.util.LinkedList;
import java.util.List;

import FrontEnd.ASTVisitor;
import Scope.Scope;
import AST.Location;

public class BlockNode extends StmtNode {

    private List<StmtNode> stmts;

    private Scope scope;

    public BlockNode(Location loc, List<StmtNode> stmts) {

        super(loc);
        this.stmts = stmts;
    }

    public List<StmtNode> stmts() {

        return stmts;
    }

    public Scope scope() {

        return scope;
    }

    public void setScope(Scope scope) {

        this.scope = scope;
    }

    @Override
    public void accept(ASTVisitor visitor) {

        visitor.visit(this);
    }

    public static BlockNode toBlockNode(StmtNode node) {

        if(node == null) return null; //node
        if(node instanceof BlockNode) return (BlockNode)node;
        LinkedList<StmtNode> stmts = new LinkedList<StmtNode>();
        stmts.add(node);
        return new BlockNode(node.location(), stmts);
    }

}
