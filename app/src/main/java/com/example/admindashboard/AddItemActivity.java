package com.example.admindashboard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class AddItemActivity extends AppCompatActivity {

    current_user_singleton currentUser = current_user_singleton.getInstance();
    private DatabaseReference restaurantsRef;
    private String restaurantId; // This will be set dynamically

    private EditText itemNameEditText;
    private EditText itemPriceEditText;
    private EditText itemDescriptionEditText;
    ImageView addImageButton;

    // Firebase
    private DatabaseReference menuItemsRef;
    private static final int PICK_IMAGE_REQUEST = 1;

    private Uri selectedImageUri; // Store selected image URI

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        restaurantsRef = database.getReference("items");

            String Userid = currentUser.getUserId();
        // Example restaurantId (replace this with your actual restaurantId)
        restaurantId = Userid.replace(".","_") ;

        // Reference the restaurant's menu items node in the database
        menuItemsRef = restaurantsRef.child(restaurantId).child("menuItems");

        itemNameEditText = findViewById(R.id.editTextText);
        itemPriceEditText = findViewById(R.id.editTextText1);
        itemDescriptionEditText = findViewById(R.id.description);
        addImageButton = findViewById(R.id.selectedImage);
        Glide.with(this).asGif().load(R.drawable.addimage).into(addImageButton);
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }

            private void openGallery() {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        findViewById(R.id.addItemButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInputValid()) {
                    if (selectedImageUri != null) {
                        uploadImageToStorage(selectedImageUri);
                    } else {
                        Toast.makeText(AddItemActivity.this, "Please select an image", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Navigate back to the previous activity
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            // Do something with the selected image URI, like display it in an ImageView
            ImageView selectedImageView = findViewById(R.id.selectedImage);
            selectedImageView.setImageURI(selectedImageUri);
        }
    }

    private boolean isInputValid() {
        String itemName = itemNameEditText.getText().toString().trim();
        String itemPriceStr = itemPriceEditText.getText().toString().trim();
        String itemDescription = itemDescriptionEditText.getText().toString().trim();

        if (itemName.isEmpty()) {
            Toast.makeText(AddItemActivity.this, "Please enter item name", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (itemPriceStr.isEmpty()) {
            Toast.makeText(AddItemActivity.this, "Please enter item price", Toast.LENGTH_SHORT).show();
            return false;
        }

        double itemPrice = Double.parseDouble(itemPriceStr);

        if (selectedImageUri == null) {
            Toast.makeText(AddItemActivity.this, "Please select an image", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    private void uploadImageToStorage(Uri imageUri) {
        String imageName = System.currentTimeMillis() + ".jpg";
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images/" + imageName);
        UploadTask uploadTask = storageRef.putFile(imageUri);

        uploadTask.addOnSuccessListener(taskSnapshot -> {
            // Get download URL from Firebase Storage
            storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                // Save image URL to the database
                String imageURL = uri.toString();
                String itemName = itemNameEditText.getText().toString().trim();
                double itemPrice = Double.parseDouble(itemPriceEditText.getText().toString().trim());
                String itemDescription = itemDescriptionEditText.getText().toString().trim();

                // Create a new menu item with the image URL
                MenuItem menuItem = new MenuItem(itemName, itemPrice, imageURL, itemDescription, restaurantId);

                // Push menu items to generate unique keys for each item
                menuItemsRef.push().setValue(menuItem)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(AddItemActivity.this, "Item added successfully", Toast.LENGTH_SHORT).show();
                            // Clear input fields
                            itemNameEditText.setText("");
                            itemPriceEditText.setText("");
                            itemDescriptionEditText.setText("");
                            addImageButton.setImageResource(R.drawable.addimage);
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(AddItemActivity.this, "Failed to add item: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            });
        }).addOnFailureListener(e -> {
            // Handle error uploading image
            Toast.makeText(AddItemActivity.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

}