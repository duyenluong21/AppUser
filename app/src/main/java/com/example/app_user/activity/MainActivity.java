package com.example.app_user.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.VoiceInteractor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.app_user.R;
import com.example.app_user.inteface.ApiService;

import org.json.JSONObject;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private SessionManager sessionManager;
    private RecyclerView RecyclerViewFlight;
    private ApiService flight;
    ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        sessionManager = new SessionManager(getApplicationContext());
        backButton = findViewById(R.id.backButton);
//        RecyclerViewFlight = findViewById(R.id.recyclerViewBookedTickets);

        flight = ApiService.searchFlight;
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}