package backend;

import AST.FunctionDefinitionNode;
import Entity.ClassEntity;
import Entity.FunctionEntity;
import Entity.StringConstantEntity;
import Entity.VariableEntity;
import IR.*;
import IR.Operand.*;
//import jdk.nashorn.internal.objects.annotations.Function;

public class Translator implements IRVisitor {

    String code = "";
    FunctionEntity currentFunction;
    private int id = 0;

    PhiReg rax = PhiReg.rax;
    PhiReg rcx = PhiReg.rcx;
    PhiReg rdx = PhiReg.rdx;
    PhiReg rbp = PhiReg.rbp;
    PhiReg rsp = PhiReg.rsp;

    private void add(String s) {
        code += s;
    }

    public void visitIns(Ins ins) {
        ins.accept(this);
    }

    public String Translate(IR ir) {

        add("global main\n\n");
        add("section .data\n");
        String s = "extern scanf\n" + "extern printf\n" + "extern puts\n" + "extern strlen\n" + "extern memcpy\n" + "extern sscanf\n" + "extern sprintf\n" + "extern malloc\n" + "extern strcmp\n" + "\n";
        add(s);

        for(VariableEntity entity : ir.globalInitializer) initVariable(entity);

        for(StringConstantEntity entity : ir.stringConstants) initString(entity);

        add("\n" + "section .text" + "\n");

        for(FunctionEntity entity : ir.ast.functionEntities()) {
            initFunction(entity);
        }
        for (ClassEntity entity : ir.ast.classEntities()) {
            for (FunctionDefinitionNode node : entity.memberFuncs()) {
                initFunction(node.entity());
            }
        }

        LoadLibrary();

        return code;
    }

    private void initVariable(VariableEntity entity) {

        add(entity.name() + "__" + ":\n");
        add("\t" + "dq" + "\t" + "0" + "\n");
    }

    private void initString(StringConstantEntity entity) {

        add("\t" + "dq" + "\t" + entity.value().length() + "\n");
        add("string__" + Integer.toString(id++) + ":\n");
        add("\t" + "db" + "\t");
        for(int i = 0; i < entity.value().length(); ++i) {
            add(Integer.toString((int)entity.value().charAt(i)) + ", ");
        }
        add("0" + "\n");
    }

    private void initFunction(FunctionEntity entity) {

        int Max = 0;
        for(int i = 0; i < entity.regList().size(); ++i) {
            int tmp = entity.regList().get(i).index() - 15;
            if(tmp > Max) Max = tmp;
        }
        if(Max % 2 == 1) ++Max;
        entity.v = 8 * Max;
        add(entity.name() + ":\n");
        currentFunction = entity;
        enterFun();
        for(Ins ins : entity.insList()) visitIns(ins);
        exitFun();

    }

    private void enterFun() {

        add("\t" + "push" + "\t" + "rbp" + "\n");
        add("\t" + "mov" + "\t\t" + "rbp, rsp" + "\n") ;
        if(currentFunction.v != 0) add("\t" + "sub" + "\t\t" + "rsp, " + Integer.toString((currentFunction.v)) + "\n");
    }

    private void exitFun() {

        if(currentFunction.v != 0) add("\t" + "add" + "\t\t" + "rsp, " + Integer.toString((currentFunction.v)) + "\n");
        add("\t" + "mov" + "\t\t" + "rsp, rbp" + "\n");
        add("\t" + "pop" + "\t\t" + "rbp" + "\n");
        add("\t" + "ret" + "\t" + "\n");
    }

    private Operand prepare(PhiReg r, Operand s) {//T t tmp d tr Tran

        if(s instanceof VirReg) return new Mem(rbp, null, 0, -(((VirReg)s).index() - 16 + 1) * 8);
        if(s instanceof Mem) {
            Mem t = (Mem)s;
            Operand base = prepare(null, t.base());
            Operand index = prepare(null, t.index());
            if(base instanceof Mem || index instanceof Mem) {
                if(index == null) add("\t" + "mov" + "\t\t" + r.toString() + ", " + base.toString() + "\n");
                else {
                    add("\t" + "mov" + "\t\t" + r.toString() + ", " + index.toString() + "\n");
                    add("\t" + "imul" + "\t" + r.toString() + ", " + Integer.toString(t.s()) + "\n");
                    add("\t" + "add" + "\t" + "\t" + r.toString() + ", " + base.toString() + "\n");
                }
                return new Mem(r, null, 0, t.d());
            }
            else return new Mem((Reg) base, (Reg) index, t.s(), t.d());
        }
        return s;
    }

    public void visit(Cjump ins){

        Operand left = prepare(rax, ins.left());
        Operand right = prepare(rcx, ins.right());

        String op;
        switch(ins.Op()) {

             case EQ : op = "je"; break;
             case NE : op = "jne"; break;
             case GT : op = "jg"; break;
             case GE : op = "jge"; break;
             case LT : op = "jl"; break;
             case LE : op = "jle"; break;
             default : throw new Error();
        }

        if(!(left instanceof Reg)) {
            add("\t" + "mov" + "\t\t" + "rax, " + left.toString() + "\n");
            left = rax;
        }
        add("\t" + "cmp" + "\t\t" + left.toString() + ", " + right.toString() + "\n");
        add("\t" + op + "\t\t" + ins.TrueLabel() + "\n");
    }

    public void visit(Assign ins) {

        Operand lhs = prepare(rcx, ins.lhs());
        Operand rhs = prepare(rax, ins.rhs()); // rax
        if(!(lhs instanceof Reg) && !(rhs instanceof Reg)) {
            add("\t" + "mov" + "\t\t" + "rax, " + rhs.toString() + "\n");
            rhs = rax;
        }
        add("\t" + "mov" + "\t\t" + lhs.toString() + ", " + rhs.toString() + "\n");
    }

    public void visit(Unary ins){
        Operand t;
        Operand s = prepare(rax, ins.Src()); //d
        boolean u = false;
        if(!(s instanceof Reg)) {
            add("\t" + "mov" + "\t\t" + "rax ," + s.toString() + "\n");
            t = rax;
            u = true;
        }
        else t = s;
        switch (ins.Op()) {
            case L_NOT :
                add("\t" + "xor" + "\t\t" + t.toString() + ", 1" + "\n");
                break;
            case MINUS :
                add("\t" + "neg" + "\t\t" + t.toString() + "\n");
                break;
            case B_NOT :
                add("\t" + "not" + "\t\t" + t.toString() + "\n");
                break;
            default: throw new Error();
        }
        if(u) add("\t" + "mov" + "\t\t" + s.toString() + ", rax" + "\n");

    }

    public void visit(Call ins) {

        int dis = 0;
        if(currentFunction.name().equals("main")) {
            if (ins.size() > 6) dis = 8 * (ins.size() - 6);
        }
        else {
            if (ins.size() > 6) {
                dis = (ins.size() - 6) * 8;
                if (ins.size() % 2 == 1) dis += 8;
            }
        }
        if(dis != 0) add("\t" + "sub" + "\t\t" + "rsp, " + Integer.toString(dis) + "\n");
        for(Ins item : ins.INS()) visitIns(item);
        if(dis != 0) add("\t" + "add" + "\t\t" + "rsp, " + Integer.toString(dis) + "\n");
    }

    public void visit(Funcall ins) {

        add("\t" + "call" + "\t" + ins.entity().name() + "\n");
    }
    public void visit(Jump ins){

        add("\t" + "jmp" + "\t\t" + ins.Label().name() + "\n");
    }

    public void visit(Label ins){
        add(ins.name() + ":\n");
    }

    private void LoadLibrary() {

        String s = "\n" + //get it from others . Thanks!
                "section .data\n" +
                "intbuffer:\n" +
                "\tdq 0\n" +
                "format1:\n" +
                "\tdb\"%lld\",0\n" +
                "format2:\n" +
                "\tdb\"%lld \",10,0\n" +
                "format3:\n" +
                "\tdb\"%s\",0\n" +
                "format4:\n" +
                "\tdb\"%s\",10,0\n" +
                "\n" +
                "section .bss\n" +
                "stringbuffer:\n" +
                "\tresb 256\n" +
                "\n" +
                "section .text\n" +
                "getInt:\n" +
                "\n" +
                "\tpush rbp\n" +
                "\tmov rbp,rsp\n" +
                "\tmov rax,0\n" +
                "\tmov rdi,format1\n" +
                "\tmov rsi,intbuffer\n" +
                "\tcall scanf\n" +
                "\tmov rax,[intbuffer]\n" +
                "\tmov rsp,rbp\n" +
                "\tpop rbp\n" +
                "\tret\n" +
                "\n" +
                "printInt:\n" +
                "\tpush rbp\n" +
                "\tmov rbp,rsp\n" +
                "\tmov rsi,rdi\n" +
                "\tmov rax,0\n" +
                "\tmov rdi,format2\n" +
                "\tcall printf\n" +
                "\tmov rsp,rbp\n" +
                "\tpop rbp\n" +
                "\tret\n" +
                "\n" +
                "Array_size:\n" +
                "\tmov rax,[rdi-8]\n" +
                "\tret\n" +
                "\n" +
                "print:\n" +
                "\tpush rbp\n" +
                "\tmov rbp,rsp\n" +
                "\tmov rax,0\n" +
                "\tmov rsi,rdi\n" +
                "\tmov rdi,format3\n" +
                "\tcall printf\n" +
                "\tmov rsp,rbp\n" +
                "\tpop rbp\n" +
                "\tret\n" +
                "\n" +
                "println:\n" +
                "\n" +
                "\tcall puts\n" +
                "\tret\n" +
                "\n" +
                "\n" +
                "transtring:\n" +
                "\n" +
                "\tpush rbp\n" +
                "\tmov rbp,rsp\n" +
                "\tcall strlen\n" +
                "\tpush rdi\n" +
                "\tmov rdi,rax\n" +
                "\tpush rdi\n" +
                "\tadd rdi,9\n" +
                "\tcall malloc\n" +
                "\tpop rdi\n" +
                "\tmov [rax],rdi\n" +
                "\tadd rax,8\n" +
                "\tmov rdx,rdi\n" +
                "\tadd rdx,1\n" +
                "\tmov rdi,rax\n" +
                "\tpop rsi\n" +
                "\tsub rsp,8\n" +
                "\tpush rax\n" +
                "\tcall memcpy\n" +
                "\tpop rax\n" +
                "\tmov rsp,rbp\n" +
                "\tpop rbp\n" +
                "\tret\n" +
                "\n" +
                "getString:\n" +
                "\n" +
                "\tpush rbp\n" +
                "\tmov rbp,rsp\n" +
                "\tmov rax,0\n" +
                "\tmov rdi,format3\n" +
                "\tmov rsi,stringbuffer\n" +
                "\tcall scanf\n" +
                "\tmov rdi,stringbuffer\n" +
                "\tcall transtring\n" +
                "\tmov rsp,rbp\n" +
                "\tpop rbp\n" +
                "\tret\n" +
                "\n" +
                "toString:\n" +
                "\n" +
                "\tpush rbp\n" +
                "\tmov rbp,rsp\n" +
                "\tmov rdx,rdi\n" +
                "\tmov rax,0\n" +
                "\tmov rdi,stringbuffer\n" +
                "\tmov rsi,format1\n" +
                "\tcall sprintf\n" +
                "\tmov rdi,stringbuffer\n" +
                "\tcall transtring\n" +
                "\tmov rsp,rbp\n" +
                "\tpop rbp\n" +
                "\tret\n" +
                "\n" +
                "String_length:\n" +
                "\n" +
                "\tmov rax,[rdi-8]\n" +
                "\tret\n" +
                "\n" +
                "String_substring:\n" +
                "\n" +
                "\tpush rbp\n" +
                "\tmov rbp,rsp\n" +
                "\tpush rdi\n" +
                "\tpush rsi\n" +
                "\tmov rdi,rdx\n" +
                "\tsub rdi,rsi\n" +
                "\tadd rdi,1\n" +
                "\tpush rdi\n" +
                "\tadd rdi,9\n" +
                "\tcall malloc\n" +
                "\tpop rdx\n" +
                "\tmov [rax],rdx\n" +
                "\tadd rax,8\n" +
                "\tpop rsi\n" +
                "\tpop rdi\n" +
                "\tadd rsi,rdi\n" +
                "\tmov rdi,rax\n" +
                "\tpush rdx\n" +
                "\tpush rax\n" +
                "\tcall memcpy\n" +
                "\tpop rax\n" +
                "\tpop rdx\n" +
                "\tmov qword[rax+rdx],0\n" +
                "\tmov rsp,rbp\n" +
                "\tpop rbp\n" +
                "\tret\n" +
                "\n" +
                "String_parseInt:\n" +
                "\n" +
                "\tmov rsi,format1\n" +
                "\tmov rdx,intbuffer\n" +
                "\tmov rax,0\n" +
                "\tcall sscanf\n" +
                "\tmov rax,[intbuffer]\n" +
                "\tret\n" +
                "\n" +
                "String_ord:\n" +
                "\n" +
                "\tmov rax,0\n" +
                "\tmov al,byte[rdi+rsi]\n" +
                "\tret\n" +
                "\n" +
                "Str_ADD:\n" +
                "\n" +
                "\tpush rbp\n" +
                "\tmov rbp,rsp\n" +
                "\tpush rsi\n" +
                "\tmov rsi,rdi\n" +
                "\tmov rdi,stringbuffer\n" +
                "\tmov rdx,[rsi-8]\n" +
                "\tpush rdx\n" +
                "\tcall memcpy\n" +
                "\tpop rdi\n" +
                "\tpop rsi\n" +
                "\tadd rdi,stringbuffer\n" +
                "\tmov rdx,[rsi-8]\n" +
                "\tadd rdx,1\n" +
                "\tcall memcpy\n" +
                "\tmov rdi,stringbuffer\n" +
                "\tcall transtring\n" +
                "\tmov rsp,rbp\n" +
                "\tpop rbp\n" +
                "\tret\n" +
                "\n" +
                "Str_LT:\n" +
                "\n" +
                "\tpush rbp\n" +
                "\tmov rbp,rsp\n" +
                "\tcall strcmp\n" +
                "\tmov rdi,0\n" +
                "\tcmp rax,0\n" +
                "\tsetl dil\n" +
                "\tmov rax,rdi\n" +
                "\tmov rsp,rbp\n" +
                "\tpop rbp\n" +
                "\tret\n" +
                "\n" +
                "Str_LE:\n" +
                "\n" +
                "\tpush rbp\n" +
                "\tmov rbp,rsp\n" +
                "\tcall strcmp\n" +
                "\tmov rdi,0\n" +
                "\tcmp rax,0\n" +
                "\tsetle dil\n" +
                "\tmov rax,rdi\n" +
                "\tmov rsp,rbp\n" +
                "\tpop rbp\n" +
                "\tret\n" +
                "\n" +
                "Str_GT:\n" +
                "\n" +
                "\tpush rbp\n" +
                "\tmov rbp,rsp\n" +
                "\tcall strcmp\n" +
                "\tmov rdi,0\n" +
                "\tcmp rax,0\n" +
                "\tsetg dil\n" +
                "\tmov rax,rdi\n" +
                "\tmov rsp,rbp\n" +
                "\tpop rbp\n" +
                "\tret\n" +
                "\n" +
                "Str_GE:\n" +
                "\n" +
                "\tpush rbp\n" +
                "\tmov rbp,rsp\n" +
                "\tcall strcmp\n" +
                "\tmov rdi,0\n" +
                "\tcmp rax,0\n" +
                "\tsetge dil\n" +
                "\tmov rax,rdi\n" +
                "\tmov rsp,rbp\n" +
                "\tpop rbp\n" +
                "\tret\n" +
                "\n" +
                "Str_EQ:\n" +
                "\n" +
                "\tpush rbp\n" +
                "\tmov rbp,rsp\n" +
                "\tcall strcmp\n" +
                "\tmov rdi,0\n" +
                "\tcmp rax,0\n" +
                "\tsete dil\n" +
                "\tmov rax,rdi\n" +
                "\tmov rsp,rbp\n" +
                "\tpop rbp\n" +
                "\tret\n" +
                "\n" +
                "Str_NE:\n" +
                "\n" +
                "\tpush rbp\n" +
                "\tmov rbp,rsp\n" +
                "\tcall strcmp\n" +
                "\tmov rdi,0\n" +
                "\tcmp rax,0\n" +
                "\tsetne dil\n" +
                "\tmov rax,rdi\n" +
                "\tmov rsp,rbp\n" +
                "\tpop rbp\n" +
                "\tret";

        add(s);
    }

    public void visit(Binary ins) {
        switch ((ins.op())) {

            case ADD :

                ins.dest = prepare(rcx, ins.dest);
                ins.left = prepare(rdx, ins.left);
                ins.right = prepare(rax, ins.right);
                if(ins.left != rdx) {
                    add("\t" + "mov" + "\t\t" + "rdx, " + ins.left.toString() + "\n");
                    ins.left = rdx;
                }
                add("\t"+ "add" +"\t" + "rdx, " + ins.right.toString() + "\n");
                add("\t" + "mov" + "\t\t" + ins.dest.toString() + ", rdx\n");
                break;
            default:
        }
    }

}
