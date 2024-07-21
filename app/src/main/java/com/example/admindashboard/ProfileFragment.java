package com.example.admindashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.admindashboard.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileFragment extends Fragment {

    private TextView tvName, tvAddress, tvDeliveryTime, tvDeliveryFee, tvEmail, tvPassword;
    private EditText etName, etAddress, etDeliveryTime, etDeliveryFee, etEmail, etPassword;
    private Button btnEditName, btnEditAddress, btnEditDeliveryTime, btnEditDeliveryFee, btnEditEmail, btnEditPassword;

    private FirebaseAuth mAuth;
    private DatabaseReference userRef;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            userRef = FirebaseDatabase.getInstance().getReference().child("Restaurants").child(uid);
        }

        // Initialize views
        tvName = view.findViewById(R.id.tvName);
        tvAddress = view.findViewById(R.id.tvAddress);
        tvDeliveryTime = view.findViewById(R.id.tvDeliveryTime);
        tvDeliveryFee = view.findViewById(R.id.tvDeliveryFee);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvPassword = view.findViewById(R.id.tvPassword);



        btnEditName = view.findViewById(R.id.btnEditName);
        btnEditAddress = view.findViewById(R.id.btnEditAddress);
        btnEditDeliveryTime = view.findViewById(R.id.btnEditDeliveryTime);
        btnEditDeliveryFee = view.findViewById(R.id.btnEditDeliveryFee);
        btnEditEmail = view.findViewById(R.id.btnEditEmail);
        btnEditPassword = view.findViewById(R.id.btnEditPassword);

        // Set click listeners for edit buttons

        btnEditAddress.setOnClickListener(v -> editAddress());
        btnEditDeliveryTime.setOnClickListener(v -> editDeliveryTime());
        btnEditDeliveryFee.setOnClickListener(v -> editDeliveryFee());
        btnEditEmail.setOnClickListener(v -> editEmail());
        btnEditPassword.setOnClickListener(v -> editPassword());

        // Populate profile information
        populateProfile();

        return view;
    }

    private void populateProfile() {
        // Retrieve profile information from Firebase
        // For demonstration purpose, I'll just populate with sample data
        tvName.setText("John Doe");
        tvAddress.setText("123 Main St");
        tvDeliveryTime.setText("30 minutes");
        tvDeliveryFee.setText("$5.00");
        tvEmail.setText("johndoe@example.com");
        tvPassword.setText("********");
    }



    private void editAddress() {
        String newAddress = etAddress.getText().toString().trim();
        if (!newAddress.isEmpty()) {
            userRef.child("address").setValue(newAddress);
            tvAddress.setText(newAddress);
            etAddress.setText("");
        }
    }

    private void editDeliveryTime() {
        String newDeliveryTime = etDeliveryTime.getText().toString().trim();
        if (!newDeliveryTime.isEmpty()) {
            userRef.child("deliveryTime").setValue(newDeliveryTime);
            tvDeliveryTime.setText(newDeliveryTime);
            etDeliveryTime.setText("");
        }
    }

    private void editDeliveryFee() {
        String newDeliveryFee = etDeliveryFee.getText().toString().trim();
        if (!newDeliveryFee.isEmpty()) {
            userRef.child("deliveryFee").setValue(newDeliveryFee);
            tvDeliveryFee.setText(newDeliveryFee);
            etDeliveryFee.setText("");
        }
    }

    private void editEmail() {
        // Implement edit functionality for email if necessary
        String newEmail = etEmail.getText().toString().trim();
        if (!newEmail.isEmpty()) {
            userRef.child("email").setValue(newEmail);
            tvDeliveryFee.setText(newEmail);
            etDeliveryFee.setText("");
        }
    }

    private void editPassword() {
        // Implement edit functionality for password if necessary
    }
}
