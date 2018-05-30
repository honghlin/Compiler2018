package backend;

import AST.FunctionDefinitionNode;
import Entity.ClassEntity;
import Entity.FunctionEntity;
import IR.*;
import IR.Operand.Reg;
//import jdk.nashorn.internal.objects.annotations.Function;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Allocator {

    private List<Ins> insSet;
    private boolean[][] edge; //  = new boolean[n][n]

    public void allocate(IR ir) {

        for (FunctionEntity entity : ir.ast().functionEntities()) {
            all(entity);
        }

        for (ClassEntity entity : ir.ast().classEntities()) {
            for (FunctionDefinitionNode node : entity.memberFuncs()) {
                all(node.entity());
            }
        }

    }

    private void all(FunctionEntity entity) {

        System.err.print(entity.name());
        System.err.print(" numOfVirtualReg ");
        System.err.print(entity.numOfVirtualReg());
        if(entity.Rec) System.err.println(" can be record");
        else System.err.println(" can not be record");
        if(entity.numOfVirtualReg() > 801) return;
        init(entity);
        LivenessAnalyzer analyzer = new LivenessAnalyzer();
        for(Ins ins : entity.insList()) analyzer.visitIns(ins);
        iterate();
        int n = entity.numOfVirtualReg();
        edge = new boolean[n][n];
        setEdge();
        coloring(n, entity);
        entity.setOptim(true);
    }

    private void setEdge() {

        for (Ins ins : insSet) {

            if (ins instanceof Jump) continue;
            if (ins instanceof Cjump) continue;
            if (ins.next != null) Edge(ins.def, ins.next.in);

        }

    }

    private void Edge(Reg def, Set<Reg> out) {

        if(def == null) return;
        for(Reg r : out) {
            edge[def.index()][r.index()] = true;
            edge[r.index()][def.index()] = true;
        }
    }

    private void coloring(int n, FunctionEntity entity) {

        int[] setColor = new int[n];
        int[] useColor = new int[n];

        for(int i = 0; i < 16; ++i) setColor[i] = i;
        for(int i = 16; i < n; ++i) setColor[i] = -1;
        for(int i = 0; i < n; ++i) useColor[i] = 0;//setColor[i] =
        entity.used = new boolean[16];

        boolean e[][] = new boolean[n][n];
        boolean d[] = new boolean[n];
        for(int i = 16; i < n; ++i)
            for(int j = 16; j < n; ++j)
                e[i][j] = edge[i][j];


        for(int i = 0; i < n; ++i) d[i] = false;

        boolean f = true;

        while(f) {

            f = false;
            for(int i = 16; i < n; ++i) {

                if(d[i]) continue;
                int k = 0;
                for(int j = 16; j < n; ++j) if(e[i][j]) ++k;

                if(k <= 10) {
                    d[i] = true;
                    f = true;
                    for(int j = 16; j < n; ++j) e[i][j] = e[j][i] = false;
                    System.err.print("delete ");
                    System.err.println(i);

                }

            }
        }

        for(int i = 0; i < 16; ++i) entity.used[i] = false;

        for(int i = 16; i < n; ++i) {

            if(d[i]) continue;

            for(int j = 0; j < n; ++j) {

                if(e[i][j] && setColor[j] != -1) useColor[setColor[j]] = i;

            }

            for(int j = 0; j <= n; j = (j == 10 ? 16 : j + 1)) { // rax rcx rdx rsp rbp

                if(useColor[j] != i) {
                    setColor[i] = j;
                    entity.regList().get(i).setIndex(j);
                    if(j < 16) entity.used[j] = true;
                    break;
                }
            }
        }

        for(int i = 16; i < n; ++i) {

                if(!d[i]) continue;

            for(int j = 0; j < n; ++j) {

                if(edge[i][j] && setColor[j] != -1) useColor[setColor[j]] = i;

            }

            for(int j = 0; j <= n; ++j) { // rax rcx rdx rsp rbp

                if(j == 11) throw new Error();
                if(useColor[j] != i) {
                    setColor[i] = j;
                    entity.regList().get(i).setIndex(j);
                    if(j < 16) entity.used[j] = true;
                    break;
                }
            }
        }

        for(int i = 16; i < n; ++i) {

            System.err.print("No.");//
            System.err.print(i);
            System.err.print(" color ");
            System.err.print(setColor[i]);
            System.err.print("\n");
        }

    }

    private void iterate() {

        boolean f = true;
        while(f) {

            f = false;
            for (int i = insSet.size() - 1; i >= 0; --i) {
                Ins ins = insSet.get(i);
                if (ins instanceof Jump) {
                    f |= compare(ins.in, ((Jump) ins).Label().in, ins.def);
                    continue;
                }
                if (ins instanceof Cjump) f |= compare(ins.in, ((Cjump) ins).TrueLabel().in, ins.def);
                if (ins.next != null) f |= compare(ins.in, ins.next.in, ins.def);

            }
        }

    }

    private boolean compare(Set<Reg> in, Set<Reg> out, Reg def) {

        boolean f = false;
        for(Reg r : out) {
            if(r != def && !in.contains(r)) {
                f = true;
                in.add(r);
            }
        }
        return f;
    }

    private void init(FunctionEntity entity) {

        insSet = new ArrayList<>();
        Ins pre = null;
        for(Ins ins : entity.insList()) {
            if(ins instanceof Call) {
                for(Ins item : ((Call)ins).INS()) {
                    insSet.add(item);
                    if(pre != null) pre.next = item;
                    pre = item;
                }
                continue;
            }
            insSet.add(ins);
            if(ins instanceof Jump) {
                ((Jump) ins).Label().prev.add(ins);
                if(pre != null) pre.next = ins;
                pre = null;
                continue;
            }
            if(ins instanceof Cjump) ((Cjump)ins).TrueLabel().prev.add(ins);
            if(pre != null) pre.next = ins;
            pre = ins;
        }

    }

}
