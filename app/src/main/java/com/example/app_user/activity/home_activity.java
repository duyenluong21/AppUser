package com.example.app_user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.app_user.R;
import com.example.app_user.model.User;

public class home_activity extends AppCompatActivity {
    private static final int PROFILE_REQUEST_CODE = 1;
    private SessionManager sessionManager;
    private User user;
    private Handler handler = new Handler();
    private Runnable runnable;
    ImageView btnLogout;

    Button btnButtonFlight;
    CardView khachhang;
    CardView cardTicket;
    CardView messCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btnButtonFlight = findViewById(R.id.btnreadFlight);
        khachhang = findViewById(R.id.khachhang);
        cardTicket = findViewById(R.id.cardTicket);
        messCard = findViewById(R.id.messCard);
        btnLogout = findViewById(R.id.btnLogout);
        sessionManager = new SessionManager(getApplicationContext());
        Intent receivedIntent = getIntent();
        if (receivedIntent != null) {
            user = receivedIntent.getParcelableExtra("object_user");
        }

        // Kiểm tra đăng nhập
        if (!sessionManager.isLoggedIn()) {
            // Chuyển đến trang đăng nhập nếu chưa đăng nhập
            redirectToLogin();
            return;
        }

        btnButtonFlight.setOnClickListener(v -> {
            Intent intent = new Intent(home_activity.this, FlightActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(view -> performLogout());

        khachhang.setOnClickListener(v -> {
            Intent intent = new Intent(home_activity.this, passenger_activity.class);
            startActivity(intent);
        });

        cardTicket.setOnClickListener(v -> {
            redirectToProfile(user);
        });

        messCard.setOnClickListener(v -> {
            Intent intent = new Intent(home_activity.this, chatUserActivity.class);
            startActivity(intent);
        });
    }

    private void performLogout() {
        sessionManager.logout();
        redirectToLogin();
    }

    private void redirectToLogin() {
        Intent intent = new Intent(home_activity.this, login.class);
        startActivity(intent);
        finish();
    }

    private void redirectToProfile(User user) {
        if (user != null) {
            Intent intent = new Intent(home_activity.this, ProfileActivity.class);
            intent.putExtra("object_user", user);
            startActivityForResult(intent, PROFILE_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PROFILE_REQUEST_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                user = data.getParcelableExtra("object_user");
                Log.d("home_activity", "Updated User: " + user);
                // Update UI or perform any action with the updated user data
            } else {
                Log.d("home_activity", "Profile activity result not OK or data is null");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        user = sessionManager.getUser();
    }
}
