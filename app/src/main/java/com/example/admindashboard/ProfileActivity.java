package com.example.admindashboard;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    EditText etName,etAddress,etPhone,profileDeliveryFee;
    TextView etEmail;
    Button  saveUserInformationButton;

    current_user_singleton currentUser = current_user_singleton.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


            etName = findViewById(R.id.profileName);
            etEmail = findViewById(R.id.profileEmail);
            etAddress = findViewById(R.id.profileAddress);
            etPhone = findViewById(R.id.profileDeliveryTime);
            profileDeliveryFee= findViewById(R.id.profileDeliveryFee);
            saveUserInformationButton = findViewById(R.id.saveUserInformationButton);

            String userId = currentUser.getUserId();
            String emailKey = userId.replace(".", "_");

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference usersRef = database.getReference("Restaurants");

            // Reference the user's node in the database
            DatabaseReference userNodeRef = usersRef.child(emailKey);

            // Retrieve the user data from the database only once
            userNodeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // User data found, dataSnapshot contains the user object
                        Restaurant user = dataSnapshot.getValue(Restaurant.class);
                        // Set the text fields with the retrieved data
                        etEmail.setText(user.getEmail());
                        etName.setText(user.getRestaurantName());
                        etPhone.setText(user.getDeliveryTime()+" ");
                        profileDeliveryFee.setText(user.getDeliveryFee()+" ");
                        etAddress.setText(user.getAddress());
                    } else {
                        // User data doesn't exist
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Failed to read value
                }
            });

            // Set click listener for the save button outside onDataChange
            saveUserInformationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String newName = etName.getText().toString().trim();
                    String newAddress = etAddress.getText().toString().trim();
                    String newDeliverTime = etPhone.getText().toString().trim();
                    String newDeliveryFee = profileDeliveryFee.getText().toString().trim();

                    int time = Integer.parseInt(newDeliverTime);
                    float fee =Float.parseFloat(newDeliveryFee);

                    // Check if any field is empty
                    if (newName.isEmpty()  || newAddress.isEmpty() || newDeliveryFee.isEmpty() || newDeliverTime.isEmpty()) {
                        Toast.makeText(ProfileActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                        return; // Exit the method if any field is empty
                    }

                    // Update user information in the database
                    userNodeRef.child("restaurantName").setValue(newName);
                    userNodeRef.child("address").setValue(newAddress);
                    userNodeRef.child("deliveryFee").setValue(fee);
                    userNodeRef.child("deliveryTime").setValue(time)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(ProfileActivity.this, "User information updated successfully", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ProfileActivity.this, "Failed to update user information: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });

    }

}