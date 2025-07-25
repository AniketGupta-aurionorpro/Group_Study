package com.aurionpro.model;

import java.util.HashSet;

public class CloneHash {
    public static void main(String[] args) {
        HashSet<String> originalSet = new HashSet<>();
        originalSet.add("Java");
        originalSet.add("Python");
        originalSet.add("C++");

        
        HashSet<String> clonedSet = (HashSet<String>) originalSet.clone();

        System.out.println("Original HashSet: " + originalSet);
        System.out.println("Cloned HashSet: " + clonedSet);
    }
}
