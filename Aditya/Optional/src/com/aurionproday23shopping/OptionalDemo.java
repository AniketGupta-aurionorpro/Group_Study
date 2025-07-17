package com.aurionproday23shopping;

import java.util.Optional;

 
public class OptionalDemo {
    public static void main(String[] args) {
        Product p1 = new Product("Laptop", Optional.of(10.0));
        Product p2 = new Product("Mouse", Optional.empty());

        p1.display();
        p2.display();
    }
}
