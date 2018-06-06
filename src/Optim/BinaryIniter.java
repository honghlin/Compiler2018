package Optim;

import FrontEnd.Visitor;

import Entity.Entity;
import FrontEnd.Visitor;

import AST.*;
import IR.Binary;
//import com.sun.org.apache.xpath.internal.operations.Mod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class BinaryIniter extends Visitor {

    //HashSet<Entity> set = new HashSet<>();

    public HashMap<Entity, Integer> map = new HashMap<>();

    private boolean Mode = true;

    private long[] siz = new long[10]; // int

    private List<Entity> enlist = new ArrayList<>();

    //public

    public BinaryOpNode init(BinaryOpNode node) { // void

        for(int i = 0; i < 10; ++i) siz[i] = 0;

        visit(node);

        // if(map.size() ) return null; != 3 == 1

        if(map.size() == 3) {

            Entity entity = enlist.get(0);

            BinaryOpNode t1 = new BinaryOpNode(BinaryOpNode.BinaryOp.MUL, new IntegerLiteralNode(new Location(0, 0), siz[map.get(entity)]), new VariableNode(entity));

            entity = enlist.get(1); // Entity

            BinaryOpNode t2 = new BinaryOpNode(BinaryOpNode.BinaryOp.MUL, new IntegerLiteralNode(new Location(0, 0), siz[map.get(entity)]), new VariableNode(entity));

            entity = enlist.get(2); // Entity

            BinaryOpNode t3 = new BinaryOpNode(BinaryOpNode.BinaryOp.MUL, new IntegerLiteralNode(new Location(0, 0), siz[map.get(entity)]), new VariableNode(entity));

            BinaryOpNode s1 = new BinaryOpNode(BinaryOpNode.BinaryOp.ADD, t1, t2);

            BinaryOpNode s2 = new BinaryOpNode(BinaryOpNode.BinaryOp.ADD, s1, t3);

            return s2;
        }

        if(map.size() == 2) {

            Entity entity = enlist.get(0);

            BinaryOpNode t1 = new BinaryOpNode(BinaryOpNode.BinaryOp.MUL, new IntegerLiteralNode(new Location(0, 0), siz[map.get(entity)]), new VariableNode(entity));

            entity = enlist.get(1); // Entity

            BinaryOpNode t2 = new BinaryOpNode(BinaryOpNode.BinaryOp.MUL, new IntegerLiteralNode(new Location(0, 0), siz[map.get(entity)]), new VariableNode(entity));

            BinaryOpNode s1 = new BinaryOpNode(BinaryOpNode.BinaryOp.ADD, t1, t2);

            return s1;
        }

        if(map.size() == 1) {

            return node;
        }

        return null;

    }

    @Override public void visit(BinaryOpNode node) {

        boolean s = true;

        switch(node.operator()) {


            case ADD:    break;
            case SUB: s = false;   break;

            //default: f = false;
        }

        visitExpr(node.left());
        if(!s) Mode = !Mode;
        visitExpr(node.right());
        if(!s) Mode = !Mode;
    }

    @Override public void visit(VariableNode node) {

        if(!map.containsKey(node.entity())) {

            enlist.add(node.entity());
            map.put(node.entity(), map.size()); //1 if(Mode)
            int t = Mode ? 1 : -1; // else ; -1 map.put(node.entity(), map.size())
            siz[map.get(node.entity())] += t;
        }// add(node.entity())  =

        else {

            int t = Mode ? 1 : -1;
            siz[map.get(node.entity())] += t;

        }
        return;
    }

}
