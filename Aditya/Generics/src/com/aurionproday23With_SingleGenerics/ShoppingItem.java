package com.aurionproday23With_SingleGenerics;

 

public class ShoppingItem<T> {
    private T item;
    private double price;

    public ShoppingItem(T item, double price) {
        this.item = item;
        this.price = price;
    }

    public T getItem() {
        return item;
    }

    public double getPrice() {
        return price;
    }

    public void display() {
        System.out.println("Item: " + item + ", Price: ₹" + price);
    }
}




