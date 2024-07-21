package com.example.admindashboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {

    ImageView imageView2;
    TextView textView5;
    CardView cardView, cardView2;
    ImageView imageView5;
    TextView pendingOrderTextView;
    CardView cardView3;
    TextView textView16;
    CardView cardView5;
    TextView textView17;
    CardView cardView4;
    TextView textView18;
    CardView cardView7;
    CardView cardView6;
    TextView textView19;
    private Button profileButton;
    current_user_singleton currentUser = current_user_singleton.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Replace with your layout file
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        imageView2 = findViewById(R.id.imageView2);
        textView5 = findViewById(R.id.textView5);
        cardView = findViewById(R.id.cardView);
        pendingOrderTextView = findViewById(R.id.pendingOrderTextView);

        ImageButton mainAdminGIF = findViewById(R.id.MainAdminGIF);
        Glide.with(this).asGif().load(R.drawable.mainadmingif).into(mainAdminGIF);
        ImageView anotherGifImageView = findViewById(R.id.addmenuGIF);
        Glide.with(this).asGif().load(R.drawable.addmenugif).into(anotherGifImageView);


        ImageView PENDINGORDERGIF= findViewById(R.id.PENDINGORDERGIF);
        Glide.with(this).asGif().load(R.drawable.pendingordergif).into(PENDINGORDERGIF);
        ImageView deliverygif= findViewById(R.id.ORDERDISPATCHGIF);
        Glide.with(this).asGif().load(R.drawable.deliveryboygif).into(deliverygif);

        ImageView restmenugif= findViewById(R.id.menuGIF);
        Glide.with(this).asGif().load(R.drawable.menugif).into(restmenugif);
        ImageView profilegif1= findViewById(R.id.PROFILEGIF);
        ImageView logoutgif1= findViewById(R.id.LOGoutGIF);
        Glide.with(this).asGif().load(R.drawable.profilegif).into(profilegif1);
        Glide.with(this).asGif().load(R.drawable.logoutgif).into(logoutgif1);


        cardView3 = findViewById(R.id.cardView3);
        textView16 = findViewById(R.id.textView16);
        cardView5 = findViewById(R.id.cardView5);
        textView17 = findViewById(R.id.textView17);
        cardView4 = findViewById(R.id.cardView4);
        textView18 = findViewById(R.id.textView18);
        cardView7 = findViewById(R.id.cardView7);
        cardView6 = findViewById(R.id.cardView6);
        textView19 = findViewById(R.id.textView19);
        cardView2 = findViewById(R.id.cardView2);



            // Set onClickListeners for each card-view
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle click for cardView
                }
            });

            cardView3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle click for cardView3
                }
            });

            anotherGifImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle click for cardView5
                    // Start AddItemActivity
                    Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                    startActivity(intent);
                }
            });

        restmenugif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click for cardView5
                // Start AddItemActivity
                Intent intent = new Intent(MainActivity.this, allItemActivity.class);
                startActivity(intent);
            }
        });

        PENDINGORDERGIF.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle click for cardView7
                    Intent intent = new Intent(MainActivity.this, PendingOrderActivity.class);
                    startActivity(intent);
                }
            });

            deliverygif.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle click for cardView6
                    Intent intent = new Intent(MainActivity.this, OutForDeliveryActivity.class);
                    startActivity(intent);
                }
            });

            cardView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sPref = getSharedPreferences("Login", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sPref.edit();
                    editor.putBoolean("isLogin", false);
                    editor.putString("userId","none");
                    editor.apply();
                    currentUser.logout();
                    Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            cardView4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this,ProfileActivity.class);
                    startActivity(intent);
                }
            });
        }
    }
