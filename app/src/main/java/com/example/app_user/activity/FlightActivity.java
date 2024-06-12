package com.example.app_user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.app_user.Adapter.FlightAdapter;
import com.example.app_user.Adapter.MyViewPager;
import com.example.app_user.R;

import com.example.app_user.model.Flight;
import com.example.app_user.inteface.ApiService;
import com.google.android.material.tabs.TabLayout;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlightActivity extends AppCompatActivity {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private MyViewPager mMyViewPager;
    private SessionManager sessionManager;
    private ApiService searchFlight;
    private ImageView backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        sessionManager = new SessionManager(getApplicationContext());
        backButton = findViewById(R.id.backButton);
        mTabLayout = findViewById(R.id.tabLayout);
        mViewPager = findViewById(R.id.viewPager);
        mMyViewPager = new MyViewPager(getSupportFragmentManager());
        mViewPager.setAdapter(mMyViewPager);
        mTabLayout.setupWithViewPager(mViewPager);
        searchFlight = ApiService.searchFlight;
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FlightActivity.this, home_activity.class);
                startActivity(intent);
                finish();

            }
        });
    }

}
