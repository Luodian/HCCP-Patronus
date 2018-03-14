/*
 * Copyright (c) 2012, Oracle and/or its affiliates. All rights reserved. DO NOT
 * ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 */
package sample.Entity;

import javafx.scene.Group;

import java.util.ArrayList;
import java.util.List;


public class Tree extends Group{

    
    List<List<Branch>> generations = new ArrayList<List<Branch>>();
    List<Branch> crown = new ArrayList<Branch>();// This branches  doesn't have child branches
    List<SmartNode> leafage = new ArrayList<SmartNode>();

    public Tree(int depth) {
        for (int i = 0; i < depth; i++) {
            generations.add(new ArrayList<Branch>());
        }
    }

}
