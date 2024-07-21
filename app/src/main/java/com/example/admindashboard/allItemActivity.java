package com.example.admindashboard;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class allItemActivity extends AppCompatActivity {
    private RecyclerView menuRecyclerView;
    private MenuAdapter menuAdapter;

    // Firebase
    private DatabaseReference menuItemsRef;
    current_user_singleton currentUser = current_user_singleton.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_item);
        String Userid = currentUser.getUserId();
        // Example restaurantId (replace this with your actual restaurantId)
       String restaurantId = Userid.replace(".","_") ;

        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        menuItemsRef = database.getReference("items").child(restaurantId).child("menuItems");

        menuRecyclerView = findViewById(R.id.menuRecyclerView);
        menuRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        menuAdapter = new MenuAdapter();
        menuRecyclerView.setAdapter(menuAdapter);

        // Retrieve items from Firebase
        menuItemsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<MenuItem> menuItems = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MenuItem menuItem = snapshot.getValue(MenuItem.class);

                    if (menuItem != null) {
                        // Check for null image
                        if (menuItem.getImageUrl() == null || menuItem.getImageUrl().isEmpty()) {
                            // Set a default image or handle it accordingly
                            // For example:
                            menuItem.setImageUrl("drawable://" + R.drawable.defaultimage);
                        }
                        menuItem.setKey(snapshot.getKey());
                        menuItems.add(menuItem);
                    }
                }
                menuAdapter.setItems(menuItems);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(allItemActivity.this, "Failed to retrieve items", Toast.LENGTH_SHORT).show();
            }
        });
        menuAdapter.setOnItemLongPressListener(new MenuAdapter.OnItemLongPressListener() {
            @Override
            public void onItemLongPressed(MenuItem menuItem) {
                // Display alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(allItemActivity.this);
                builder.setMessage("Are you sure you want to delete this item?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Remove item from database
                                menuItemsRef.child(menuItem.getKey()).removeValue()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(allItemActivity.this, "Item deleted successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(allItemActivity.this, "Failed to delete item", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });



        // Inside onCreate method of allItemActivity
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Navigate back to the previous activity
            }
        });
    }
}

