package com.example.deliverytracker;

import java.util.List;

public class CartItem {
    private String name;
    private double basePrice;
    private int quantity;
    private String selectedSauce;
    private List<String> selectedToppings;
    private int imageResource;

    public CartItem(String name, double basePrice, int quantity, String selectedSauce,
                    List<String> selectedToppings, int imageResource) {
        this.name = name;
        this.basePrice = basePrice;
        this.quantity = quantity;
        this.selectedSauce = selectedSauce;
        this.selectedToppings = selectedToppings;
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public double getPrice() {
        return basePrice * quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getSelectedSauce() {
        return selectedSauce;
    }

    public List<String> getSelectedToppings() {
        return selectedToppings;
    }

    public int getImageResource() {
        return imageResource;
    }
}