package backend;

import IR.*;

public interface IRVisitor {

    void visit(Assign xins);
    void visit(Binary ins);
    void visit(Call ins);
    void visit(Cjump ins);
    void visit(Funcall ins);
    //void visit(Ins ins);
    void visit(Jump ins);
    void visit(Label ins);
    void visit(Unary ins);

}
