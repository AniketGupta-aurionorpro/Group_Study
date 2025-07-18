package com.aurionpro.model;

import java.util.ArrayList;
import java.util.List;

public class ArrayRotator {
	public static void main(String[] args) {
		List<Integer> numbers = List.of(1,2,3,4,5,6);
		List<Integer> rotated = new ArrayList<>();
		int rotationIndex = 2;
		List<Integer> subList = numbers.subList(0, rotationIndex);
		rotated.addAll(numbers.subList(rotationIndex, numbers.size()));
		rotated.addAll(subList);
		
		System.out.println(rotated);
	}
}
