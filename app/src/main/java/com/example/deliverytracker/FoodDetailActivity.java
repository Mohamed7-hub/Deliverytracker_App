package com.example.deliverytracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class FoodDetailActivity extends AppCompatActivity {
    private int quantity = 1;
    private double basePrice;
    private ImageView foodImage;
    private TextView foodTitleText, quantityText, priceText;
    private Button addToCartButton;
    private CheckBox omeletCheck, sausageCheck, cheeseCheck;
    private RadioButton teriyakiRadio, yakinikuRadio;
    private int foodImageResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        initializeViews();
        loadFoodDetails();
        setupClickListeners();
        updateTotalPrice();
    }

    private void initializeViews() {
        foodImage = findViewById(R.id.foodImage);
        foodTitleText = findViewById(R.id.foodTitle);
        quantityText = findViewById(R.id.quantityText);
        priceText = findViewById(R.id.priceText);
        addToCartButton = findViewById(R.id.addToCartButton);
        omeletCheck = findViewById(R.id.omeletCheck);
        sausageCheck = findViewById(R.id.sausageCheck);
        cheeseCheck = findViewById(R.id.cheeseCheck);
        teriyakiRadio = findViewById(R.id.teriyakiRadio);
        yakinikuRadio = findViewById(R.id.yakinikuRadio);
    }

    private void loadFoodDetails() {
        Intent intent = getIntent();
        foodImageResource = intent.getIntExtra("food_image", R.drawable.food_1);
        foodImage.setImageResource(foodImageResource);
        foodTitleText.setText(intent.getStringExtra("food_name"));
        String priceString = intent.getStringExtra("food_price");
        if (priceString != null && priceString.startsWith("$")) {
            basePrice = Double.parseDouble(priceString.substring(1));
        } else {
            basePrice = 0.00; // Default price if parsing fails
        }
        priceText.setText(String.format("$%.2f", basePrice));
    }

    private void setupClickListeners() {
        ImageView decreaseButton = findViewById(R.id.decreaseButton);
        ImageView increaseButton = findViewById(R.id.increaseButton);

        decreaseButton.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                updateQuantityAndPrice();
            }
        });

        increaseButton.setOnClickListener(v -> {
            quantity++;
            updateQuantityAndPrice();
        });

        omeletCheck.setOnCheckedChangeListener((buttonView, isChecked) -> updateTotalPrice());
        sausageCheck.setOnCheckedChangeListener((buttonView, isChecked) -> updateTotalPrice());
        cheeseCheck.setOnCheckedChangeListener((buttonView, isChecked) -> updateTotalPrice());

        addToCartButton.setOnClickListener(v -> {
            addToCart();
            Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void updateQuantityAndPrice() {
        quantityText.setText(String.valueOf(quantity));
        updateTotalPrice();
    }

    private void updateTotalPrice() {
        double totalPrice = calculateTotalPrice();
        priceText.setText(String.format("$%.2f", totalPrice));
        addToCartButton.setText(String.format("Add to Cart ($%.2f)", totalPrice));
    }

    private double calculateTotalPrice() {
        double totalPrice = basePrice * quantity;

        // Add topping prices
        if (omeletCheck.isChecked()) totalPrice += 2.00 * quantity;
        if (sausageCheck.isChecked()) totalPrice += 3.00 * quantity;
        if (cheeseCheck.isChecked()) totalPrice += 5.00 * quantity;

        return totalPrice;
    }

    private void addToCart() {
        List<String> selectedToppings = new ArrayList<>();
        if (omeletCheck.isChecked()) selectedToppings.add("Omelet");
        if (sausageCheck.isChecked()) selectedToppings.add("Sausage");
        if (cheeseCheck.isChecked()) selectedToppings.add("Cheese");

        String sauce = teriyakiRadio.isChecked() ? "Teriyaki" : "Yakiniku";

        CartItem cartItem = new CartItem(
                foodTitleText.getText().toString(),
                calculateTotalPrice(),
                quantity,
                sauce,
                selectedToppings,
                foodImageResource
        );

        Cart.getInstance().addItem(cartItem);
    }
}