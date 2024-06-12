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

import com.example.app_user.Adapter.PassengerAdapter;
import com.example.app_user.R;
import com.example.app_user.inteface.ApiService;
import com.example.app_user.model.passenger;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class passenger_activity extends AppCompatActivity {
    private List<passenger> passengers;
    private SessionManager sessionManager;
    private PassengerAdapter passengerAdapter;
    ImageView backButton ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger);
        backButton = findViewById(R.id.backButton);
        sessionManager = new SessionManager(getApplicationContext());
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(passenger_activity.this, home_activity.class);
                startActivity(intent);
                finish();

            }
        });
        // Khởi tạo RecyclerView và Adapter
        RecyclerView recyclerView = findViewById(R.id.recyclerViewBookedTickets);
        passengerAdapter = new PassengerAdapter();

        // Thiết lập LayoutManager cho RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Gắn Adapter vào RecyclerView
        recyclerView.setAdapter(passengerAdapter);

        // Gọi API để lấy danh sách chuyến bay
        getListPassenger();

    }

    private void getListPassenger() {
        ApiService.searchFlight.getListUser(new HashMap<>()).enqueue(new Callback<ApiResponse<List<passenger>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<passenger>>> call, Response<ApiResponse<List<passenger>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<passenger>> apiResponse = response.body();
                    if (apiResponse.getData() != null) {
                        passengers = apiResponse.getData();
                        // Cập nhật dữ liệu mới cho Adapter
                        passengerAdapter.setPassengers(passengers);
                        // Thông báo rằng dữ liệu đã thay đổi
                        passengerAdapter.notifyDataSetChanged();
                    }
                } else {
                    Log.e("API Error", "Error: " + response.code() + " " + response.message());
                    Toast.makeText(passenger_activity.this, "Call Api error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<passenger>>> call, Throwable t) {
                Log.e("API Error", "Error: " + t.getMessage());
                Toast.makeText(passenger_activity.this, "Call Api error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
