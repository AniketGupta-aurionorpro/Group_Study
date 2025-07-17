package com.aurionproday23basic;

 
import java.util.Optional;

public class OptionalBasic {
    public static void main(String[] args) {
        // Creating Optional with a value
        Optional<String> name = Optional.of("Aditya");

        // Creating an empty Optional
        Optional<String> empty = Optional.empty();

        // Using isPresent() to check if value exists
        if (name.isPresent()) {
            System.out.println("Name: " + name.get());
        }

        // Using ifPresent()
        name.ifPresent(n -> System.out.println("Hello, " + n));

        // Using orElse()
        String result = empty.orElse("Default Name");
        System.out.println("Result: " + result);
    }
}
