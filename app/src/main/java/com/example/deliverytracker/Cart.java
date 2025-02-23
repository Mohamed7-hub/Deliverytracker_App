package com.example.deliverytracker;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private static Cart instance;
    private List<CartItem> items;
    private List<CartListener> listeners;

    private Cart() {
        items = new ArrayList<>();
        listeners = new ArrayList<>();
    }

    public static Cart getInstance() {
        if (instance == null) {
            instance = new Cart();
        }
        return instance;
    }

    public void addItem(CartItem item) {
        items.add(item);
        notifyListeners();
    }

    public void removeItem(int position) {
        if (position >= 0 && position < items.size()) {
            items.remove(position);
            notifyListeners();
        }
    }

    public void updateItemQuantity(int position, int newQuantity) {
        if (position >= 0 && position < items.size()) {
            if (newQuantity <= 0) {
                items.remove(position);
            } else {
                CartItem item = items.get(position);
                CartItem updatedItem = new CartItem(
                        item.getName(),
                        item.getBasePrice(),
                        newQuantity,
                        item.getSelectedSauce(),
                        item.getSelectedToppings(),
                        item.getImageResource()
                );
                items.set(position, updatedItem);
            }
            notifyListeners();
        }
    }

    public void clearCart() {
        items.clear();
        notifyListeners();
    }

    public List<CartItem> getItems() {
        return items;
    }

    public int getItemCount() {
        return items.size();
    }

    public double getTotal() {
        double total = 0;
        for (CartItem item : items) {
            total += item.getPrice();
        }
        return total;
    }

    public interface CartListener {
        void onCartUpdated();
    }

    public void addListener(CartListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(CartListener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners() {
        for (CartListener listener : listeners) {
            listener.onCartUpdated();
        }
    }
}