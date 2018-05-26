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

    StringBuffer code = new StringBuffer("");
    FunctionEntity currentFunction;
    private int id = 0;

    PhiReg rax = PhiReg.rax;
    PhiReg rcx = PhiReg.rcx;
    PhiReg rdx = PhiReg.rdx;
    PhiReg rbp = PhiReg.rbp;
    PhiReg rsp = PhiReg.rsp;

    private void add(String s) {

        code.append(s);
    }

    public void visitIns(Ins ins) {
        ins.accept(this);
    }

    public StringBuffer Translate(IR ir) {

        add("global main\n\n");
        String s = "extern scanf\n" + "extern printf\n" + "extern puts\n" + "extern strlen\n" + "extern memcpy\n" + "extern sscanf\n" + "extern sprintf\n" + "extern malloc\n" + "extern strcmp\n" + "\n";
        add(s);
        add("section .data\n");

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

        add("\t" + "dq" + "\t" + Integer.toString(entity.value().length() - 14) + "\n");
        add("string__" + Integer.toString(id++) + ":\n");
        add("\t" + "db" + "\t");
        for(int i = 14; i < entity.value().length(); ++i) {
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
        if(entity.used != null) {
            for (int i = 0; i < 11; ++i) if (entity.used[i]) entity.MaxReg += 1;
        }
        add(entity.name() + ":\n");
        currentFunction = entity;
        enterFun();
        for(Ins ins : entity.insList()) visitIns(ins);
        exitFun();

    }

    int save;

    private void enterFun() {

        add("\t" + "push" + "\t" + "rbp" + "\n");
        add("\t" + "mov" + "\t\t" + "rbp, rsp" + "\n") ;
        save = 0;
        if(currentFunction.isOptim() && !currentFunction.name().equals("main")) {
            for (int i = 0; i < 5; ++i) {
                if(!currentFunction.used[PhiReg.getCallee(i).index()]) continue;
                add("\t" + "push" + "\t" + PhiReg.getCallee(i).toString() + "\n");
                ++save;
            }
        }
        if(save != 0) add("\t" + "sub" + "\t\t" + "rbp, " + Integer.toString((8 * save)) + "\n");
        if(currentFunction.v != 0) add("\t" + "sub" + "\t\t" + "rsp, " + Integer.toString((currentFunction.v)) + "\n");
    }

    private void exitFun() {

        if(currentFunction.v != 0) add("\t" + "add" + "\t\t" + "rsp, " + Integer.toString((currentFunction.v)) + "\n");
        if(save != 0) add("\t" + "add" + "\t\t" + "rbp, " + Integer.toString((8 * save)) + "\n");
        if(currentFunction.isOptim() && !currentFunction.name().equals("main")) {
            for (int i = 4; i >= 0; --i) {
                if(!currentFunction.used[PhiReg.getCallee(i).index()]) continue;
                add("\t" + "pop" + "\t\t" + PhiReg.getCallee(i).toString() + "\n");
            }
        }
        add("\t" + "mov" + "\t\t" + "rsp, rbp" + "\n");
        add("\t" + "pop" + "\t\t" + "rbp" + "\n");
        add("\t" + "ret" + "\t" + "\n");
    }

    private Operand prepare(PhiReg r, Operand s) {//T t tmp d tr Tran

        if(s instanceof VirReg) {
            VirReg t = (VirReg)s;
            if(t.index() < 16) return PhiReg.ToX86(t.index());
            return new Mem(rbp, null, 0, -(((VirReg)s).index() - 16 + 1) * 8);
        }
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
        if(lhs == null || rhs == null) {
            int zky = 0;
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
                if ((ins.size() +  currentFunction.MaxReg) % 2 == 1) dis += 8;
            }
            else if(currentFunction.MaxReg % 2 == 1) dis += 8;
        }
        if(dis != 0) add("\t" + "sub" + "\t\t" + "rsp, " + Integer.toString(dis) + "\n");
        if(currentFunction.isOptim()) {
            for (int i = 0; i < 6; ++i) {
                if(!currentFunction.used[PhiReg.getCaller(i).index()]) continue;
                add("\t" + "push" + "\t" + PhiReg.getCaller(i).toString() + "\n");
            }
        }
        for(Ins item : ins.INS()) {
            visitIns(item);
            if(item instanceof Funcall && currentFunction.isOptim()) {
                for (int i = 5; i >= 0; --i) {
                    if(!currentFunction.used[PhiReg.getCaller(i).index()]) continue;
                    add("\t" + "pop" + "\t\t" + PhiReg.getCaller(i).toString() + "\n");
                }
            }
        }
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
                "\n" +
                "section .data\n" +
                "intbuffer:\n" +
                "    dq 0\n" +
                "format1:\n" +
                "    db\"%lld\",0\n" +
                "format2:\n" +
                "    db\"%lld \",10,0\n" +
                "format3:\n" +
                "    db\"%s\",0\n" +
                "format4:\n" +
                "    db\"%s\",10,0\n" +
                "\n" +
                "section .bss\n" +
                "stringbuffer:\n" +
                "    resb 256\n" +
                "\n" +
                "section .text\n" +
                "getInt:\n" +
                "\n" +
                "    push rbp\n" +
                "    mov rbp,rsp\n" +
                "    mov rax,0\n" +
                "    mov rdi,format1\n" +
                "    mov rsi,intbuffer\n" +
                "    call scanf\n" +
                "    mov rax,[intbuffer]\n" +
                "    mov rsp,rbp\n" +
                "    pop rbp\n" +
                "    ret\n" +
                "\n" +
                "printInt:\n" +
                "    push rbp\n" +
                "    mov rbp,rsp\n" +
                "    mov rsi,rdi\n" +
                "    mov rax,0\n" +
                "    mov rdi,format2\n" +
                "    call printf\n" +
                "    mov rsp,rbp\n" +
                "    pop rbp\n" +
                "    ret\n" +
                "\n" +
                "size:\n" +
                "    mov rax,[rdi-8]\n" +
                "    ret\n" +
                "\n" +
                "print:\n" +
                "    push rbp\n" +
                "    mov rbp,rsp\n" +
                "    mov rax,0\n" +
                "    mov rsi,rdi\n" +
                "    mov rdi,format3\n" +
                "    call printf\n" +
                "    mov rsp,rbp\n" +
                "    pop rbp\n" +
                "    ret\n" +
                "\n" +
                "println:\n" +
                "\n" +
                "    call puts\n" +
                "    ret\n" +
                "\n" +
                "\n" +
                "transtring:\n" +
                "\n" +
                "    push rbp\n" +
                "    mov rbp,rsp\n" +
                "    call strlen\n" +
                "    push rdi\n" +
                "    mov rdi,rax\n" +
                "    push rdi\n" +
                "    add rdi,9\n" +
                "    call malloc\n" +
                "    pop rdi\n" +
                "    mov [rax],rdi\n" +
                "    add rax,8\n" +
                "    mov rdx,rdi\n" +
                "    add rdx,1\n" +
                "    mov rdi,rax\n" +
                "    pop rsi\n" +
                "    sub rsp,8\n" +
                "    push rax\n" +
                "    call memcpy\n" +
                "    pop rax\n" +
                "    mov rsp,rbp\n" +
                "    pop rbp\n" +
                "    ret\n" +
                "\n" +
                "getString:\n" +
                "\n" +
                "    push rbp\n" +
                "    mov rbp,rsp\n" +
                "    mov rax,0\n" +
                "    mov rdi,format3\n" +
                "    mov rsi,stringbuffer\n" +
                "    call scanf\n" +
                "    mov rdi,stringbuffer\n" +
                "    call transtring\n" +
                "    mov rsp,rbp\n" +
                "    pop rbp\n" +
                "    ret\n" +
                "\n" +
                "\n" +
                "ALIGN   16\n" +
                "\n" +
                "toString:\n" +
                "        push    rbx\n" +
                "        mov     rbx, rdi\n" +
                "        mov     edi, 20\n" +
                "        call    malloc\n" +
                "        test    rbx, rbx\n" +
                "        mov     r9, rax\n" +
                "        lea     rdi, [rax+8H]\n" +
                "        js      L_007\n" +
                "        jne     L_010\n" +
                "        lea     rcx, [rax+9H]\n" +
                "        mov     byte [rax+8H], 48\n" +
                "        mov     rsi, rcx\n" +
                "L_004:  mov     rax, rcx\n" +
                "        mov     byte [rcx], 0\n" +
                "        sub     rax, rdi\n" +
                "        mov     qword [r9], rax\n" +
                "        lea     rax, [rcx-1H]\n" +
                "        cmp     rax, rsi\n" +
                "        jc      L_006\n" +
                "L_005:  movzx   edx, byte [rsi]\n" +
                "        movzx   ecx, byte [rax]\n" +
                "        add     rsi, 1\n" +
                "        sub     rax, 1\n" +
                "        mov     byte [rsi-1H], cl\n" +
                "        mov     byte [rax+1H], dl\n" +
                "        cmp     rsi, rax\n" +
                "        jbe     L_005\n" +
                "L_006:  mov     rax, rdi\n" +
                "        pop     rbx\n" +
                "        ret\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "ALIGN   8\n" +
                "L_007:  lea     rsi, [rax+9H]\n" +
                "        mov     byte [rax+8H], 45\n" +
                "        neg     rbx\n" +
                "L_008:  mov     rcx, rsi\n" +
                "        mov     r8, qword 6666666666666667H\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "ALIGN   16\n" +
                "L_009:  mov     rax, rbx\n" +
                "        add     rcx, 1\n" +
                "        imul    r8\n" +
                "        mov     rax, rbx\n" +
                "        add     ebx, 48\n" +
                "        sar     rax, 63\n" +
                "        sar     rdx, 2\n" +
                "        sub     rdx, rax\n" +
                "        lea     eax, [rdx+rdx*4]\n" +
                "        add     eax, eax\n" +
                "        sub     ebx, eax\n" +
                "        test    rdx, rdx\n" +
                "        mov     byte [rcx-1H], bl\n" +
                "        mov     rbx, rdx\n" +
                "        jnz     L_009\n" +
                "        jmp     L_004\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "ALIGN   8\n" +
                "L_010:  mov     rsi, rdi\n" +
                "        jmp     L_008\n" +
                "\n" +
                "\n" +
                "\n" +
                "length:\n" +
                "\n" +
                "    mov rax,[rdi-8]\n" +
                "    ret\n" +
                "\n" +
                "\n" +
                "ALIGN   8\n" +
                "\n" +
                "substring:\n" +
                "        push    r13\n" +
                "        sub     rdx, rsi\n" +
                "        push    r12\n" +
                "        push    rbp\n" +
                "        push    rbx\n" +
                "        mov     r12, rdi\n" +
                "        lea     rbx, [rdx+1H]\n" +
                "        lea     rdi, [rdx+0AH]\n" +
                "        mov     rbp, rsi\n" +
                "        sub     rsp, 8\n" +
                "        call    malloc\n" +
                "        test    rbx, rbx\n" +
                "        mov     r13, rax\n" +
                "        mov     qword [rax], rbx\n" +
                "        lea     rcx, [rax+8H]\n" +
                "        jle     L_003\n" +
                "        lea     rsi, [r12+rbp]\n" +
                "        mov     rdi, rcx\n" +
                "        mov     rdx, rbx\n" +
                "        call    memcpy\n" +
                "        mov     rcx, rax\n" +
                "L_003:  mov     byte [r13+rbx+8H], 0\n" +
                "        add     rsp, 8\n" +
                "        mov     rax, rcx\n" +
                "        pop     rbx\n" +
                "        pop     rbp\n" +
                "        pop     r12\n" +
                "        pop     r13\n" +
                "        ret\n" +
                "\n" +
                "\n" +
                "        nop\n" +
                "\n" +
                "\n" +
                "parseInt:\n" +
                "\n" +
                "    mov rsi,format1\n" +
                "    mov rdx,intbuffer\n" +
                "    mov rax,0\n" +
                "    call sscanf\n" +
                "    mov rax,[intbuffer]\n" +
                "    ret\n" +
                "\n" +
                "ord:\n" +
                "\n" +
                "    mov rax,0\n" +
                "    mov al,byte[rdi+rsi]\n" +
                "    ret\n" +
                "    \n" +
                "Str_ADD:\n" +
                "        push    r15\n" +
                "        push    r14\n" +
                "        mov     r15, rdi\n" +
                "        push    r13\n" +
                "        push    r12\n" +
                "        mov     r14, rsi\n" +
                "        push    rbp\n" +
                "        push    rbx\n" +
                "        sub     rsp, 8\n" +
                "        mov     rbp, qword [rdi-8H]\n" +
                "        mov     r12, qword [rsi-8H]\n" +
                "        lea     rbx, [rbp+r12]\n" +
                "        lea     rdi, [rbx+9H]\n" +
                "        call    malloc\n" +
                "        test    rbp, rbp\n" +
                "        mov     qword [rax], rbx\n" +
                "        mov     r13, rax\n" +
                "        lea     rbx, [rax+8H]\n" +
                "        jle     L_001\n" +
                "        mov     rdx, rbp\n" +
                "        mov     rsi, r15\n" +
                "        mov     rdi, rbx\n" +
                "        call    memcpy\n" +
                "L_001:  add     rbx, rbp\n" +
                "        test    r12, r12\n" +
                "        jle     L_002\n" +
                "        lea     rdi, [r13+rbp+8H]\n" +
                "        mov     rdx, r12\n" +
                "        mov     rsi, r14\n" +
                "        call    memcpy\n" +
                "L_002:  mov     byte [rbx+r12], 0\n" +
                "        mov     rax, rbx\n" +
                "        add     rsp, 8\n" +
                "        pop     rbx\n" +
                "        sub     rax, rbp\n" +
                "        pop     rbp\n" +
                "        pop     r12\n" +
                "        pop     r13\n" +
                "        pop     r14\n" +
                "        pop     r15\n" +
                "        ret\n" +
                "\n" +
                "\n" +
                "Str_LT:\n" +
                "\n" +
                "    push rbp\n" +
                "    mov rbp,rsp\n" +
                "    call strcmp\n" +
                "    mov rdi,0\n" +
                "    cmp rax,0\n" +
                "    setl dil\n" +
                "    mov rax,rdi\n" +
                "    mov rsp,rbp\n" +
                "    pop rbp\n" +
                "    ret\n" +
                "\n" +
                "Str_LE:\n" +
                "\n" +
                "    push rbp\n" +
                "    mov rbp,rsp\n" +
                "    call strcmp\n" +
                "    mov rdi,0\n" +
                "    cmp rax,0\n" +
                "    setle dil\n" +
                "    mov rax,rdi\n" +
                "    mov rsp,rbp\n" +
                "    pop rbp\n" +
                "    ret\n" +
                "\n" +
                "Str_GT:\n" +
                "\n" +
                "    push rbp\n" +
                "    mov rbp,rsp\n" +
                "    call strcmp\n" +
                "    mov rdi,0\n" +
                "    cmp rax,0\n" +
                "    setg dil\n" +
                "    mov rax,rdi\n" +
                "    mov rsp,rbp\n" +
                "    pop rbp\n" +
                "    ret\n" +
                "\n" +
                "Str_GE:\n" +
                "\n" +
                "    push rbp\n" +
                "    mov rbp,rsp\n" +
                "    call strcmp\n" +
                "    mov rdi,0\n" +
                "    cmp rax,0\n" +
                "    setge dil\n" +
                "    mov rax,rdi\n" +
                "    mov rsp,rbp\n" +
                "    pop rbp\n" +
                "    ret\n" +
                "\n" +
                "Str_EQ:\n" +
                "\n" +
                "    push rbp\n" +
                "    mov rbp,rsp\n" +
                "    call strcmp\n" +
                "    mov rdi,0\n" +
                "    cmp rax,0\n" +
                "    sete dil\n" +
                "    mov rax,rdi\n" +
                "    mov rsp,rbp\n" +
                "    pop rbp\n" +
                "    ret\n" +
                "\n" +
                "Str_NE:\n" +
                "\n" +
                "    push rbp\n" +
                "    mov rbp,rsp\n" +
                "    call strcmp\n" +
                "    mov rdi,0\n" +
                "    cmp rax,0\n" +
                "    setne dil\n" +
                "    mov rax,rdi\n" +
                "    mov rsp,rbp\n" +
                "    pop rbp\n" +
                "    ret\n";

        add(s);
    }

    public void visit(Binary ins) {
        switch ((ins.op())) {

            case ADD :

                ins.dest = prepare(rcx, ins.dest);
                ins.left = prepare(rdx, ins.left);
                ins.right = prepare(rax, ins.right);
                if (ins.dest instanceof Reg && ins.left instanceof Reg && (ins.right instanceof Reg || ins.right instanceof Imm)){
                    add("\t" + "lea" + "\t\t" + ins.dest.toString() + ", [" + ins.left.toString()+ " + " + ins.right.toString() + "]" + "\n");
                    return;
                }
                if(ins.left != rdx) {
                    add("\t" + "mov" + "\t\t" + "rdx, " + ins.left.toString() + "\n");
                    ins.left = rdx;
                }
                add("\t"+ "add" +"\t\t" + "rdx, " + ins.right.toString() + "\n");
                add("\t" + "mov" + "\t\t" + ins.dest.toString() + ", rdx\n");
                break;

            case SUB :

                ins.dest = prepare(rcx, ins.dest);
                ins.left = prepare(rdx, ins.left);
                ins.right = prepare(rax, ins.right);
                if(ins.left != rdx) {
                    add("\t" + "mov" + "\t\t" + "rdx, " + ins.left.toString() + "\n");
                    ins.left = rdx;
                }
                add("\t"+ "sub" +"\t\t" + "rdx, " + ins.right.toString() + "\n");
                add("\t" + "mov" + "\t\t" + ins.dest.toString() + ", rdx\n");
                break;

            case MUL :

                ins.dest = prepare(rcx, ins.dest);
                ins.left = prepare(rdx, ins.left);
                ins.right = prepare(rax, ins.right);
                if(ins.left != rdx) {
                    add("\t" + "mov" + "\t\t" + "rdx, " + ins.left.toString() + "\n");
                    ins.left = rdx;
                }
                add("\t"+ "imul" +"\t\t" + "rdx, " + ins.right.toString() + "\n");
                add("\t" + "mov" + "\t\t" + ins.dest.toString() + ", rdx\n");
                break;

            case B_XOR :

                ins.dest = prepare(rcx, ins.dest);
                ins.left = prepare(rdx, ins.left);
                ins.right = prepare(rax, ins.right);
                if(ins.left != rdx) {
                    add("\t" + "mov" + "\t\t" + "rdx, " + ins.left.toString() + "\n");
                    ins.left = rdx;
                }
                add("\t"+ "xor" +"\t\t" + "rdx, " + ins.right.toString() + "\n");
                add("\t" + "mov" + "\t\t" + ins.dest.toString() + ", rdx\n");
                break;

            case B_AND :

                ins.dest = prepare(rcx, ins.dest);
                ins.left = prepare(rdx, ins.left);
                ins.right = prepare(rax, ins.right);
                if(ins.left != rdx) {
                    add("\t" + "mov" + "\t\t" + "rdx, " + ins.left.toString() + "\n");
                    ins.left = rdx;
                }
                add("\t"+ "and" +"\t" + "rdx, " + ins.right.toString() + "\n");
                add("\t" + "mov" + "\t\t" + ins.dest.toString() + ", rdx\n");
                break;

            case B_OR :

                ins.dest = prepare(rcx, ins.dest);
                ins.left = prepare(rdx, ins.left);
                ins.right = prepare(rax, ins.right);
                if(ins.left != rdx) {
                    add("\t" + "mov" + "\t\t" + "rdx, " + ins.left.toString() + "\n");
                    ins.left = rdx;
                }
                add("\t"+ "or" +"\t" + "rdx, " + ins.right.toString() + "\n");
                add("\t" + "mov" + "\t\t" + ins.dest.toString() + ", rdx\n");
                break;

            case EQ :

                ins.dest = prepare(rax, ins.dest);
                ins.left = prepare(rcx, ins.left);//
                ins.right = prepare(rdx, ins.right);//
                if(!(ins.left instanceof Reg) && !(ins.right instanceof Reg)) {
                    add("\t" + "mov" + "\t\t" + "rcx, " + ins.left.toString() + "\n");
                    ins.left = rcx;
                }
                if(ins.left instanceof Imm) {
                    add("\t" + "mov" + "\t\t" + "rcx, " + ins.left.toString() + "\n");
                    ins.left = rcx;
                }
                add("\t" + "cmp" + "\t\t" + ins.left.toString() + ", " + ins.right.toString() + "\n");
                add("\t" + "sete" + "\t" + "cl" + "\n");
                add("\t" + "movzx" + "\t" + "rcx," + "cl" + "\n");
                add("\t" + "mov" + "\t\t" + ins.dest.toString() + ", rcx" + "\n");
                break;

            case NE :

                ins.dest = prepare(rax, ins.dest);
                ins.left = prepare(rcx, ins.left);//
                ins.right = prepare(rdx, ins.right);//
                if(!(ins.left instanceof Reg) && !(ins.right instanceof Reg)) {
                    add("\t" + "mov" + "\t\t" + "rcx, " + ins.left.toString() + "\n");
                    ins.left = rcx;
                }
                if(ins.left instanceof Imm) {
                    add("\t" + "mov" + "\t\t" + "rcx, " + ins.left.toString() + "\n");
                    ins.left = rcx;
                }
                add("\t" + "cmp" + "\t\t" + ins.left.toString() + ", " + ins.right.toString() + "\n");
                add("\t" + "setne" + "\t" + "cl" + "\n");
                add("\t" + "movzx" + "\t" + "rcx," + "cl" + "\n");
                add("\t" + "mov" + "\t\t" + ins.dest.toString() + ", rcx" + "\n");
                break;

            case LT :

                ins.dest = prepare(rax, ins.dest);
                ins.left = prepare(rcx, ins.left);//
                ins.right = prepare(rdx, ins.right);//
                if(!(ins.left instanceof Reg) && !(ins.right instanceof Reg)) {
                    add("\t" + "mov" + "\t\t" + "rcx, " + ins.left.toString() + "\n");
                    ins.left = rcx;
                }
                if(ins.left instanceof Imm) {
                    add("\t" + "mov" + "\t\t" + "rcx, " + ins.left.toString() + "\n");
                    ins.left = rcx;
                }
                add("\t" + "cmp" + "\t\t" + ins.left.toString() + ", " + ins.right.toString() + "\n");
                add("\t" + "setl" + "\t" + "cl" + "\n");
                add("\t" + "movzx" + "\t" + "rcx," + "cl" + "\n");
                add("\t" + "mov" + "\t\t" + ins.dest.toString() + ", rcx" + "\n");
                break;

            case LE :

                ins.dest = prepare(rax, ins.dest);
                ins.left = prepare(rcx, ins.left);//
                ins.right = prepare(rdx, ins.right);//
                if(!(ins.left instanceof Reg) && !(ins.right instanceof Reg)) {
                    add("\t" + "mov" + "\t\t" + "rcx, " + ins.left.toString() + "\n");
                    ins.left = rcx;
                }
                if(ins.left instanceof Imm) {
                    add("\t" + "mov" + "\t\t" + "rcx, " + ins.left.toString() + "\n");
                    ins.left = rcx;
                }
                add("\t" + "cmp" + "\t\t" + ins.left.toString() + ", " + ins.right.toString() + "\n");
                add("\t" + "setle" + "\t" + "cl" + "\n");
                add("\t" + "movzx" + "\t" + "rcx," + "cl" + "\n");
                add("\t" + "mov" + "\t\t" + ins.dest.toString() + ", rcx" + "\n");
                break;

            case GT :

                ins.dest = prepare(rax, ins.dest);
                ins.left = prepare(rcx, ins.left);//
                ins.right = prepare(rdx, ins.right);//
                if(!(ins.left instanceof Reg) && !(ins.right instanceof Reg)) {
                    add("\t" + "mov" + "\t\t" + "rcx, " + ins.left.toString() + "\n");
                    ins.left = rcx;
                }
                if(ins.left instanceof Imm) {
                    add("\t" + "mov" + "\t\t" + "rcx, " + ins.left.toString() + "\n");
                    ins.left = rcx;
                }
                add("\t" + "cmp" + "\t\t" + ins.left.toString() + ", " + ins.right.toString() + "\n");
                add("\t" + "setg" + "\t" + "cl" + "\n");
                add("\t" + "movzx" + "\t" + "rcx," + "cl" + "\n");
                add("\t" + "mov" + "\t\t" + ins.dest.toString() + ", rcx" + "\n");
                break;

            case GE :

                ins.dest = prepare(rax, ins.dest);
                ins.left = prepare(rcx, ins.left);//
                ins.right = prepare(rdx, ins.right);//
                if(!(ins.left instanceof Reg) && !(ins.right instanceof Reg)) {
                    add("\t" + "mov" + "\t\t" + "rcx, " + ins.left.toString() + "\n");
                    ins.left = rcx;
                }
                if(ins.left instanceof Imm) {
                    add("\t" + "mov" + "\t\t" + "rcx, " + ins.left.toString() + "\n");
                    ins.left = rcx;
                }
                add("\t" + "cmp" + "\t\t" + ins.left.toString() + ", " + ins.right.toString() + "\n");
                add("\t" + "setge" + "\t" + "cl" + "\n");
                add("\t" + "movzx" + "\t" + "rcx," + "cl" + "\n");
                add("\t" + "mov" + "\t\t" + ins.dest.toString() + ", rcx" + "\n");
                break;

            case DIV :

                ins.dest = prepare(null, ins.dest);
                ins.left = prepare(rax, ins.left);
                if(ins.left != rax) {
                    add("\t" + "mov" + "\t\t" + "rax, " + ins.left.toString() + "\n");
                    ins.left = rax;
                }
                ins.right = prepare(rcx, ins.right);
                if(ins.right != rcx) {
                    add("\t" + "mov" + "\t\t" + "rcx, " + ins.right.toString() + "\n");
                    ins.right = rcx;
                }
                add("\t" + "cqo" + "\n");
                add("\t" + "idiv" + "\t" + "ecx" + "\n");
                add("\t" + "mov" + "\t\t" +  ins.dest.toString() +  ", rax\n");
                break;

            case MOD :

                ins.dest = prepare(null, ins.dest);
                ins.left = prepare(rax, ins.left);
                if(ins.left != rax) {
                    add("\t" + "mov" + "\t\t" + "rax, " + ins.left.toString() + "\n");
                    ins.left = rax;
                }
                ins.right = prepare(rcx, ins.right);
                if(ins.right != rcx) {
                    add("\t" + "mov" + "\t\t" + "rcx, " + ins.right.toString() + "\n");
                    ins.right = rcx;
                }
                add("\t" + "cqo" + "\n");
                add("\t" + "idiv" + "\t" + "ecx" + "\n");
                add("\t" + "mov" + "\t\t" +  ins.dest.toString() +  ", rdx\n");
                break;

            case LSHIFT :

                if(ins.dest instanceof Imm && ins.right instanceof Imm) { //
                    int t = 0;
                    t = (int)((Imm)ins.dest).value() << (int)((Imm)ins.dest).value();//
                    add("\t" + "mov" + "\t\t" + ins.dest.toString() + ", " + Integer.toString(t) + "\n");
                    return;
                }
                ins.right = prepare(rcx, ins.right);
                if(ins.right != rcx) {
                    add("\t" + "mov" + "\t\t" + "rcx, " +  ins.right.toString() +  "\n");
                    ins.right = rcx;
                }
                ins.left = prepare(rax, ins.left);
                if(ins.left != rax) {
                    add("\t" + "mov" + "\t\t" + "rax, " +  ins.left.toString() +  "\n");
                    ins.left = rax;
                }
                add("\t" + "shl" + "\t\t" + "eax, cl" + "\n");
                ins.dest = prepare(rcx, ins.dest);
                add("\t" + "mov" + "\t\t" + ins.dest + ", rax" + "\n");
                break;

            case RSHIFT :

                if(ins.dest instanceof Imm && ins.right instanceof Imm) { //
                    int t = 0;
                    t = (int)((Imm)ins.dest).value() >> (int)((Imm)ins.dest).value();//
                    add("\t" + "mov" + "\t\t" + ins.dest.toString() + ", " + Integer.toString(t) + "\n");
                    return;
                }
                ins.right = prepare(rcx, ins.right);
                if(ins.right != rcx) {
                    add("\t" + "mov" + "\t\t" + "rcx, " +  ins.right.toString() +  "\n");
                    ins.right = rcx;
                }
                ins.left = prepare(rax, ins.left);
                if(ins.left != rax) {
                    add("\t" + "mov" + "\t\t" + "rax, " +  ins.left.toString() +  "\n");
                    ins.left = rax;
                }
                add("\t" + "sar" + "\t\t" + "eax, cl" + "\n");
                ins.dest = prepare(rcx, ins.dest);
                add("\t" + "mov" + "\t\t" + ins.dest + ", rax" + "\n");
                break;

            default:
        }
    }

}
