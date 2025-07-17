package com.aurionproday23shopping;

 
import java.util.Optional;

class Product {
    private String name;
    private Optional<Double> discount; // This can be absent

    public Product(String name, Optional<Double> discount) {
        this.name = name;
        this.discount = discount;
    }

    public void display() {
        System.out.println("Product: " + name);
        System.out.println("Discount: " + discount.orElse(0.0) + "%");
    }
}
 