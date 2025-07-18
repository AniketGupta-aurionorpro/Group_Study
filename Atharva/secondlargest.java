package com.aurionpro.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class secondlargest {
	public static void main(String[] args) {

		List<Integer> numbers = new ArrayList<>(List.of(4, 2, 6, 1, 7));

        Optional<Integer> max = numbers.stream().max(Integer::compareTo);
        numbers.remove((Integer) max.get()); 

        Optional<Integer> secondMax = numbers.stream().max(Integer::compareTo);
        System.out.println("second Largest:"+(Integer)secondMax.get()); 
	}
}
