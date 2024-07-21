package com.example.admindashboard;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PendingOrderActivity extends AppCompatActivity {
    private RecyclerView pendingOrderRecyclerView;
    private PendingOrderAdapter pendingOrderAdapter;
    private DatabaseReference pendingOrderRef;
    current_user_singleton currentUser = current_user_singleton.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_order);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        String Userid = currentUser.getUserId();
        // Example restaurantId (replace this with your actual restaurantId)
       String restaurantId = Userid.replace(".","_") ;


        pendingOrderRecyclerView = findViewById(R.id.pendingOrderRecyclerView);
        pendingOrderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        pendingOrderAdapter = new PendingOrderAdapter();
        pendingOrderRecyclerView.setAdapter(pendingOrderAdapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        pendingOrderRef = database.getReference("Orders").child(restaurantId);
        //deliveryRef = database.getReference("out_for_delivery");

        pendingOrderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<OrderItem> pendingOrders = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrderId(snapshot.child("orderId").getValue(String.class));
                    orderItem.setOrderPrice(snapshot.child("orderPrice").getValue(String.class));
                    orderItem.setOrderStatus(snapshot.child("orderStatus").getValue(String.class));

                    orderItem.setKey(snapshot.getKey());
                    pendingOrders.add(orderItem);
                }
                pendingOrderAdapter.setItems(pendingOrders);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PendingOrderActivity.this, "Failed to retrieve pending orders", Toast.LENGTH_SHORT).show();
            }
        });

        pendingOrderAdapter.setOnOrderActionListener(new PendingOrderAdapter.OnOrderActionListener() {
            @Override
            public void onAccept(OrderItem orderItem) {
                // Update status to "IN PROGRESS" in database
                Button acceptButton = findViewById(R.id.orderacceptButton);
                acceptButton.setEnabled(false);

                pendingOrderRef.child(orderItem.getKey()).child("orderStatus").setValue("IN PROGRESS")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                acceptButton.setEnabled(true);
                                // Hide loading indicator
                                if (task.isSuccessful()) {
                                    Toast.makeText(PendingOrderActivity.this, "Order accepted", Toast.LENGTH_SHORT).show();

                                    // Remove the order from the "Orders" node
                                    pendingOrderRef.child(orderItem.getKey()).removeValue()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Order removed from the "Orders" node
                                                    // You may perform any additional actions here if needed
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Failed to remove the order
                                                    Toast.makeText(PendingOrderActivity.this, "Failed to remove order", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                    // Remove the order item from the pending list
                                    int position = pendingOrderAdapter.getPendingOrders().indexOf(orderItem);
                                    if (position != -1) {

                                        pendingOrderAdapter.removeItem(position);
                                        pendingOrderAdapter.notifyItemRemoved(position);

                                    }

                                    // Update order status in the order item
                                    orderItem.setOrderStatus("IN PROGRESS");
                                    // Add the order item to the "Out for Delivery" node in the database
                                    String Userid = currentUser.getUserId();
                                    String restaurantId2 = Userid.replace(".","_") ;
                                    DatabaseReference restaurantOrderRef = FirebaseDatabase.getInstance().getReference().child("out_for_delivery").child(restaurantId2);
                                    DatabaseReference outForDeliveryRef = restaurantOrderRef.push();
                                    outForDeliveryRef.setValue(orderItem)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Successfully added to "Out for Delivery"
                                                    // You may perform any additional actions here if needed
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Failed to add to "Out for Delivery"
                                                    Toast.makeText(PendingOrderActivity.this, "Failed to add order to Out for Delivery", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }
                        });

            }

            @Override
            public void onReject(OrderItem orderItem) {
                // Update status to "CANCELLED" in database
                pendingOrderRef.child(orderItem.getKey()).child("orderStatus").setValue("CANCELLED")
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(PendingOrderActivity.this, "Order rejected", Toast.LENGTH_SHORT).show();

                                pendingOrderRef.child(orderItem.getKey()).removeValue()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // Order removed from the "Orders" node
                                                // You may perform any additional actions here if needed
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Failed to remove the order
                                                Toast.makeText(PendingOrderActivity.this, "Failed to remove order", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                int position = pendingOrderAdapter.getPendingOrders().indexOf(orderItem);
                                if (position != -1) {
                                    pendingOrderAdapter.removeItem(position);
                                    pendingOrderAdapter.notifyItemRemoved(position);
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(PendingOrderActivity.this, "Failed to reject order", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}
