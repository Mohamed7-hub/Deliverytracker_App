package com.example.deliverytracker;

public class FoodItem {
    private String name;
    private String price;
    private int imageResource;
    private String category;

    public FoodItem(String name, String price, int imageResource, String category) {
        this.name = name;
        this.price = price;
        this.imageResource = imageResource;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getCategory() {
        return category;
    }
}