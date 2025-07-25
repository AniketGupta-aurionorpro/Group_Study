package com.aurionpro.model;

import java.util.Arrays;
import java.util.HashSet;

public class HashToArraySort{
    public static void main(String[] args) {
        HashSet<Integer> set = new HashSet<>();
        set.add(20);
        set.add(5);
        set.add(15);
        set.add(10);

        // Convert to array
        Integer[] array = set.toArray(new Integer[0]);

        // Sort array
        Arrays.sort(array);

        System.out.println("Sorted Array: " + Arrays.toString(array));
    }
}

