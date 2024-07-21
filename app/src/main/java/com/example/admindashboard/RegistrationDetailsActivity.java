package com.example.admindashboard;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

public class RegistrationDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_details);


        // Handle account type selection (e.g., from radio button click)
        // Launch appropriate fragment based on account type
        showRestaurantAdminRegistrationFragment();

    }


    private void showRestaurantAdminRegistrationFragment() {
        Fragment fragment = new RestaurantAdminRegistrationFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}