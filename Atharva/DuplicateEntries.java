package com.aurionpro.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DuplicateEntries {
	public static void main(String[] args) {
		List<Integer> numbers = List.of(1,2,3,4,4,5,2,7,8,8,3);
		Set<Integer> visitedNumbers = new HashSet<>();;
		List<Integer> collect = numbers.stream().filter(i->!visitedNumbers.add(i)).distinct().collect(Collectors.toList());
		System.out.println(collect);
	}

}
