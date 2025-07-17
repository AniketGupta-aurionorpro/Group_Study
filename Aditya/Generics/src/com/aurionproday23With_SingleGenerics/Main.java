package com.aurionproday23With_SingleGenerics;

 
public class Main {
    public static void main(String[] args) {
        ShoppingItem<String> grocery = new ShoppingItem<>("Sugar", 45.5);
        ShoppingItem<String> electronic = new ShoppingItem<>("Mouse", 799.99);
        ShoppingItem<Integer> productCode = new ShoppingItem<>(12345, 1500);

        grocery.display();
        electronic.display();
        productCode.display();
    }
}


