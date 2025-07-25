package com.aurionpro.model;

import java.util.HashSet;

public class ItterateHash {

	    public static void main(String[] args) {
	        HashSet<String> set = new HashSet<>();
	        set.add("bat");
	        set.add("ball");
	        set.add("stump");

	        System.out.println("Iterating over HashSet:");
	        for (String item : set) {
	            System.out.println(item);
	        }
	    }
}
