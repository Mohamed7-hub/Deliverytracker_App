package com.example.deliverytracker;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.chip.ChipGroup;
import java.util.ArrayList;
import java.util.List;

public class OrdersActivity extends AppCompatActivity implements Cart.CartListener {
    private RecyclerView recyclerView;
    private FoodAdapter foodAdapter;
    private List<FoodItem> allFoodItems;
    private EditText searchEditText;
    private String currentCategory = "All";
    private String currentSearchQuery = "";
    private ImageView cartIcon;
    private TextView cartBadge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> onBackPressed());

        initializeViews();
        setupRecyclerView();
        setupChipGroup();
        setupSearchListener();
        setupCartIcon();
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.recyclerView);
        searchEditText = findViewById(R.id.searchEditText);
        cartIcon = findViewById(R.id.cartButton);
        cartBadge = findViewById(R.id.cartBadge);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        allFoodItems = getAllFoodItems();
        foodAdapter = new FoodAdapter(allFoodItems);
        recyclerView.setAdapter(foodAdapter);
    }

    private void setupChipGroup() {
        ChipGroup chipGroup = findViewById(R.id.chipGroup);
        chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            currentCategory = getCategoryFromChipId(checkedId);
            filterItems();
        });
    }

    private void setupSearchListener() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentSearchQuery = s.toString().toLowerCase().trim();
                filterItems();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupCartIcon() {
        cartIcon.setOnClickListener(v -> {
            startActivity(new Intent(OrdersActivity.this, CartActivity.class));
        });
        updateCartIcon();
        Cart.getInstance().addListener(this);
    }

    private void filterItems() {
        List<FoodItem> filteredList = new ArrayList<>();

        for (FoodItem item : allFoodItems) {
            boolean categoryMatch = currentCategory.equals("All") || item.getCategory().equals(currentCategory);
            boolean searchMatch = item.getName().toLowerCase().contains(currentSearchQuery);

            if (categoryMatch && searchMatch) {
                filteredList.add(item);
            }
        }

        foodAdapter.updateItems(filteredList);
    }

    private String getCategoryFromChipId(int checkedId) {
        if (checkedId == R.id.chip_all) return "All";
        if (checkedId == R.id.chip_featured) return "Featured";
        if (checkedId == R.id.chip_top) return "Top of Week";
        if (checkedId == R.id.chip_soup) return "Soup";
        if (checkedId == R.id.chip_seafood) return "Seafood";
        return "All";
    }

    private List<FoodItem> getAllFoodItems() {
        List<FoodItem> items = new ArrayList<>();
        items.add(new FoodItem("Black Pepper Beef", "$27.12", R.drawable.food_1, "Featured"));
        items.add(new FoodItem("Chicken Noodle Soup", "$12.50", R.drawable.food_3, "Featured"));
        items.add(new FoodItem("Grilled Salmon", "$22.99", R.drawable.food_4, "Featured"));
        items.add(new FoodItem("Tomato Basil Soup", "$10.99", R.drawable.food_4, "Featured"));
        items.add(new FoodItem("Vegetable Stir Fry", "$15.75", R.drawable.food_5, "Top of Week"));
        items.add(new FoodItem("Shrimp Scampi", "$20.50", R.drawable.food_3, "Top of Week"));
        items.add(new FoodItem("Tomato Basil Soup", "$10.99", R.drawable.food_4, "Top of Week"));
        items.add(new FoodItem("Beef Stroganoff", "$18.25", R.drawable.food_2, "Top of Week"));
        items.add(new FoodItem("Classic Chicken Soup", "$18.25", R.drawable.food_s2, "Soup"));
        items.add(new FoodItem("Tomato Basil Soup", "$10.99", R.drawable.food_s1, "Soup"));
        items.add(new FoodItem("Chicken Soup", "$18.25", R.drawable.food_s3, "Soup"));
        items.add(new FoodItem("Tomato Basil Soup", "$18.25", R.drawable.food_s, "Soup"));
        items.add(new FoodItem("Tomato Basil Soup", "$10.99", R.drawable.food_sea, "Seafood"));
        items.add(new FoodItem("Honey Dijon Glaze", "$18.25", R.drawable.food_sea2, "Seafood"));
        items.add(new FoodItem("Tomato Basil Soup", "$10.99", R.drawable.food_sea1, "Seafood"));
        items.add(new FoodItem("Lime Garlic Butter", "$18.25", R.drawable.food_sea3, "Seafood"));
        return items;
    }

    private void updateCartIcon() {
        int itemCount = Cart.getInstance().getItemCount();
        if (itemCount > 0) {
            cartBadge.setVisibility(View.VISIBLE);
            cartBadge.setText(String.valueOf(itemCount));
        } else {
            cartBadge.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCartUpdated() {
        updateCartIcon();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartIcon();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Cart.getInstance().removeListener(this);
    }
}