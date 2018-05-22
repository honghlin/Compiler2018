package IR.Operand;

public class PhiReg extends Reg{

    public static PhiReg rdi = new PhiReg("rdi", 0);
    public static PhiReg rsi = new PhiReg("rsi", 1);
    public static PhiReg rbx = new PhiReg("rbx", 2);
    public static PhiReg r8  = new PhiReg("r8", 3);
    public static PhiReg r9  = new PhiReg("r9", 4);
    public static PhiReg r10 = new PhiReg("r10", 5);
    public static PhiReg r11 = new PhiReg("r11", 6);
    public static PhiReg r12 = new PhiReg("r12", 7);
    public static PhiReg r13 = new PhiReg("r13", 8);
    public static PhiReg r14 = new PhiReg("r14", 9);
    public static PhiReg r15 = new PhiReg("r15", 10);
    public static PhiReg rdx = new PhiReg("rdx", 11);
    public static PhiReg rcx = new PhiReg("rcx", 12);
    public static PhiReg rax = new PhiReg("rax", 13);
    public static PhiReg rbp = new PhiReg("rbp", 14);
    public static PhiReg rsp = new PhiReg("rsp", 15);

    public PhiReg(String name,int n) {
        this.name = name;
        this.index = n;
    }

    @Override public String toString() {
        return name;
    }

    public static PhiReg getByRank(int i) {
        if(i == 0) return rdi;
        if(i == 1) return rsi;
        if(i == 2) return rbx;
        if(i == 3) return r8;
        if(i == 4) return r9;
        if(i == 5) return r10;
        if(i == 6) return r11;
        if(i == 7) return r12;
        if(i == 8) return r13;
        if(i == 9) return r14;
        if(i == 10) return r15;
        if(i == 11) return rdx;
        if(i == 12) return rcx;
        if(i == 13) return rax;
        if(i == 14) return rbp;
        if(i == 15) return rsp;
        throw new Error();
    }

    public static PhiReg ToX86(int i) {
        if(i == 0) return rdi;
        if(i == 1) return rsi;
        if(i == 2) return rbx;
        if(i == 3) return r8;
        if(i == 4) return r9;
        if(i == 5) return r10;
        if(i == 6) return r11;
        if(i == 7) return r12;
        if(i == 8) return r13;
        if(i == 9) return r14;
        if(i == 10) return r15;
        throw new Error();
    }

    public static PhiReg getCaller(int i) {
        if(i == 0) return r10;
        if(i == 1) return r11;
        if(i == 2) return rdi;
        if(i == 3) return rsi;
        if(i == 4) return r8;
        if(i == 5) return r9;
        throw new Error();
    }

    public static PhiReg getCallee(int i) {
        if(i == 0) return rbx;
        if(i == 1) return r12;
        if(i == 2) return r13;
        if(i == 3) return r14;
        if(i == 4) return r15;
        throw new Error();
    }


    public static PhiReg getParameterReg(int i) {
        if (i == 0) return rdi;
        if (i == 1) return rsi;
        if (i == 2) return rdx;
        if (i == 3) return rcx;
        if (i == 4) return r8;
        if (i == 5) return r9;
        throw new Error();
    }


}
