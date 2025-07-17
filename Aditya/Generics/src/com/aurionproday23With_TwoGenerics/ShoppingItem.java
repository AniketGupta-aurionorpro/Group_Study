package com.aurionproday23With_TwoGenerics;

 
public class ShoppingItem<T, U> {
    private T item;
    private U category;
    private double price;

    public ShoppingItem(T item, U category, double price) {
        this.item = item;
        this.category = category;
        this.price = price;
    }

    public T getItem() {
        return item;
    }

    public U getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public void display() {
        System.out.println("Item: " + item + ", Category: " + category + ", Price: ₹" + price);
    }
}

