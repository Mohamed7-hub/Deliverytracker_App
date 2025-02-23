package com.example.deliverytracker;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CartActivity extends AppCompatActivity implements Cart.CartListener {
    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private TextView orderCountText;
    private TextView emptyCartText;
    private Button placeOrderButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initializeViews();
        setupRecyclerView();
        updateCartUI();
        Cart.getInstance().addListener(this);

        placeOrderButton.setOnClickListener(v -> {
            if (Cart.getInstance().getItemCount() > 0) {
                Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_SHORT).show();
                Cart.getInstance().clearCart();
                finish();
            }
        });
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.cartRecyclerView);
        orderCountText = findViewById(R.id.orderCountText);
        emptyCartText = findViewById(R.id.emptyCartText);
        placeOrderButton = findViewById(R.id.placeOrderButton);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CartAdapter(Cart.getInstance().getItems());
        recyclerView.setAdapter(adapter);
    }

    private void updateCartUI() {
        Cart cart = Cart.getInstance();
        int itemCount = cart.getItemCount();

        orderCountText.setText(String.format("Your Order (%d)", itemCount));
        placeOrderButton.setText(String.format("Place Order ($%.2f)", cart.getTotal()));

        if (itemCount == 0) {
            emptyCartText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            placeOrderButton.setVisibility(View.GONE);
        } else {
            emptyCartText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            placeOrderButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCartUpdated() {
        updateCartUI();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Cart.getInstance().removeListener(this);
    }
}