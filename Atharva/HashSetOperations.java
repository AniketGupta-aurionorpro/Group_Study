package com.aurionpro.test;

import java.util.*;

public class HashSetOperations {

    // 1. Append Element to HashSet
    public static void appendElement(HashSet<String> set, String element) {
        set.add(element); 
        System.out.println("After appending: " + set);
    }

    // 2. Iterate HashSet Elements
    public static void iterateElements(HashSet<String> set) {
        System.out.println("Iterating through HashSet:");
        for (String item : set) {
            System.out.println(item);
        }
    }

    // 3. Clone HashSet
    public static HashSet<String> cloneHashSet(HashSet<String> set) {
        HashSet<String> cloned = (HashSet<String>) set.clone();
        System.out.println("Cloned HashSet: " + cloned);
        return cloned;
    }

    // 4. Convert HashSet to Array and Sort
    public static void convertAndSort(HashSet<Integer> intSet) {
        Integer[] array = intSet.toArray(new Integer[0]);
        Arrays.sort(array);
        System.out.println("Sorted array: " + Arrays.toString(array));
    }

    // 5. Find differences between two HashSets
    public static void findDifferences(HashSet<String> set1, HashSet<String> set2) {
        HashSet<String> onlyInSet1 = new HashSet<>(set1);
        onlyInSet1.removeAll(set2);
        System.out.println("Only in set1: " + onlyInSet1);

        HashSet<String> onlyInSet2 = new HashSet<>(set2);
        onlyInSet2.removeAll(set1);
        System.out.println("Only in set2: " + onlyInSet2);
    }

    public static void main(String[] args) {
        // Task 1: Append
        HashSet<String> hashSet1 = new HashSet<>(Arrays.asList("Red", "Yellow", "Blue"));
        appendElement(hashSet1, "Green");

        // Task 2: Iterate
        iterateElements(hashSet1);

        // Task 3: Clone
        HashSet<String> clonedSet = cloneHashSet(hashSet1);

        // Task 4: Convert & Sort
        HashSet<Integer> intSet = new HashSet<>(Arrays.asList(10, 50, 40,30, 20));
        convertAndSort(intSet);

        // Task 5: Find Differences
        HashSet<String> hashSet2 = new HashSet<>(Arrays.asList("Red", "White", "Blue", "Black", "Orange", "Green"));
        findDifferences(hashSet1, hashSet2);
    }
}

