package com.Aurionpro;

 
import java.util.StringTokenizer;

public class StringT {

    public static void main(String[] args) {
        // Format: item:quantity:price
        String cartItems = "Milk:2:30, Bread:1:25, Eggs:12:5";

        StringTokenizer itemTokenizer = new StringTokenizer(cartItems, ",");

        System.out.println("Shopping Cart Details:");
        while (itemTokenizer.hasMoreTokens()) {
            String itemData = itemTokenizer.nextToken().trim();
            StringTokenizer details = new StringTokenizer(itemData, ":");

            String itemName = details.nextToken();
            int quantity = Integer.parseInt(details.nextToken());
            double price = Double.parseDouble(details.nextToken());

            System.out.println("Item: " + itemName + ", Quantity: " + quantity + ", Price: ₹" + price);
        }
    }
}