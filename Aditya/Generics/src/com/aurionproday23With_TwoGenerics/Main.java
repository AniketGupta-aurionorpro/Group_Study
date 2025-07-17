package com.aurionproday23With_TwoGenerics;

 
public class Main {
    public static void main(String[] args) {
        //  String for item, String for category
        ShoppingItem<String, String> grocery = new ShoppingItem<>("Sugar", "Grocery", 45.5);

        //  String for item, String for category
        ShoppingItem<String, String> electronic = new ShoppingItem<>("Mouse", "Electronics", 799.99);

        //  Integer for item  and String for category
        ShoppingItem<Integer, String> productCodeItem = new ShoppingItem<>(12345, "Home Appliances", 1500);

        
        grocery.display();
        electronic.display();
        productCodeItem.display();
    }
}


