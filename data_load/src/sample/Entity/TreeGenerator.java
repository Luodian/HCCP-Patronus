/*
 * Copyright (c) 2012, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 */
package sample.Entity;

import javafx.scene.Group;
import javafx.scene.control.Tooltip;
import sample.Entity.Branch.Type;
import sample.Utils.RandomUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static sample.Main.allNodes;
import static sample.Utils.Util.addChildToParent;


public class TreeGenerator {

    public Group content;
    public int treeDepth;

    public TreeGenerator(Group content, int treeDepth) {
        this.content = content;
        this.treeDepth = treeDepth;
    }

    public Tree generateTree(int rootAngle) {
        Tree tree = new Tree(treeDepth);
        addChildToParent(content, tree);

        Branch root = new Branch(rootAngle);
        addChildToParent(tree, root);
        tree.generations.get(0).add(root); //root branch

        for (int i = 1; i < treeDepth; i++) {
            for (Branch parentBranch : tree.generations.get(i - 1)) {
                List<Branch> newBranches = generateBranches(rootAngle, parentBranch, i);
                if (newBranches.isEmpty()) {
                    tree.crown.add(parentBranch);
                }
                tree.generations.get(i).addAll(generateBranches(rootAngle, parentBranch, i));
            }
        }
        for (Branch crownBranch : tree.generations.get(treeDepth - 1)) {
            tree.crown.add(crownBranch);
        }
        tree.leafage.addAll(generateLeafage(tree.crown));
        return tree;
    }

    private List<Branch> generateBranches(int rootAngle, Branch parentBranch, int depth) {
        List<Branch> branches = new ArrayList<Branch>();
        if (parentBranch == null) { // add root branch
            branches.add(new Branch(rootAngle));
        } else {
            if (parentBranch.length < 10) {
                return Collections.emptyList();
            }
            branches.add(new Branch(rootAngle, parentBranch, Type.LEFT, depth)); //add side left branch
            branches.add(new Branch(rootAngle, parentBranch, Type.RIGHT, depth)); // add side right branch
            branches.add(new Branch(rootAngle, parentBranch, Type.TOP, depth)); //add top branch
        }

        return branches;
    }

    private List<SmartNode> generateLeafage(List<Branch> crown) {
        List<SmartNode> leafage = new ArrayList<SmartNode>();
        for (int i = 0; i < crown.size(); i++) {
            Branch branch = crown.get(i);
            String[] hospitals = {"黑龙江中医药","哈医大一附院","哈工大校医院","哈医大二附院"};
            SmartNode leaf = new SmartNode(branch, new Tooltip(hospitals[RandomUtil.getRandomIndex(0,3)]));
            allNodes.add(leaf);
            leafage.add(leaf);
            addChildToParent(branch, leaf);
        }
        return leafage;
    }

}
