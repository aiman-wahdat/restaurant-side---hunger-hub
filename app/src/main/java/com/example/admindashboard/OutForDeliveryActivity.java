package com.example.admindashboard;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OutForDeliveryActivity extends AppCompatActivity {
    private RecyclerView deliveryRecyclerView;
    private DeliveryAdapter deliveryAdapter;
    current_user_singleton currentUser = current_user_singleton.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out_for_delivery);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        List<OrderItem> orderItemList = new ArrayList<>();
        String Userid = currentUser.getUserId();
        String restaurantId2 = Userid.replace(".","_") ;
        DatabaseReference restaurantOrderRef = FirebaseDatabase.getInstance().getReference().child("out_for_delivery").child(restaurantId2);

        deliveryRecyclerView = findViewById(R.id.deliveryRecyclerView);
        deliveryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        deliveryAdapter = new DeliveryAdapter();
        deliveryRecyclerView.setAdapter(deliveryAdapter);

        restaurantOrderRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear existing orders
                orderItemList.clear();

                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    // Iterate through each order under the restaurant
                    OrderItem orderItem = orderSnapshot.getValue(OrderItem.class); // Assuming OrderItem is your data model
                    orderItemList.add(orderItem);
                }
                deliveryAdapter.setItems(orderItemList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }
}
