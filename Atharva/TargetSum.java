package com.aurionpro.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TargetSum {
	public static void main(String[] args) {
		List<Integer> numbers = List.of(1,2,3,4,5);
		Set<List<Integer>> pairs = new HashSet<>();
		int target = 6;
		
		for(int i=0;i<numbers.size()/2;i++) {
			for (int j=0;j<numbers.size();j++) {
				if(i==j) {
					continue;
				}
				if(numbers.get(i)+numbers.get(j) == target) {
					pairs.add(List.of(Math.min(numbers.get(i),numbers.get(j)),Math.max(numbers.get(i),numbers.get(j))));
				}
			}
		}
		System.out.println(pairs);
	}

}
