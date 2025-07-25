package com.aurionpro.model;

import java.util.HashSet;

public class HashSetDiffere {
    public static void main(String[] args) {
        HashSet<String> set1 = new HashSet<>();
        set1.add("A");
        set1.add("B");
        set1.add("C");
        set1.add("D");

        HashSet<String> set2 = new HashSet<>();
        set2.add("C");
        set2.add("D");
        set2.add("E");
        set2.add("F");

        // Unique in set1
        HashSet<String> uniqueSet1 = new HashSet<>(set1);
        uniqueSet1.removeAll(set2);

        // Unique in set2
        HashSet<String> uniqueSet2 = new HashSet<>(set2);
        uniqueSet2.removeAll(set1);

        System.out.println("Unique elements in set1: " + uniqueSet1);
        System.out.println("Unique elements in set2: " + uniqueSet2);
    }
}
