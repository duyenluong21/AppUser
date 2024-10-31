package com.example.app_user.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.app_user.inteface.ApiService;
import com.example.app_user.R;
import com.example.app_user.model.Passenger;
import com.example.app_user.model.SumTicket;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PassengerDetail extends AppCompatActivity {
    private EditText txtHoVaTen, txtEmail, txtDiaChi, txtSoDienThoai, txtChuyenBay, txtSoLuong;
    ImageView backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passengerdetail);
        backButton = findViewById(R.id.backButton);
        txtHoVaTen = findViewById(R.id.txtHoVaTen);
        txtEmail = findViewById(R.id.txtEmail);
        txtDiaChi = findViewById(R.id.txtDiaChi);
        txtSoDienThoai = findViewById(R.id.txtSoDienThoai);
        txtChuyenBay = findViewById(R.id.txtChuyenBay);
        txtSoLuong = findViewById(R.id.txtSoLuong);
        // Get the maKH from intent
        String maKH = getIntent().getStringExtra("maKH");

        // Fetch passenger details based on maKH
        if (maKH != null) {
            fetchPassengerDetails(maKH);
            fetchSumTickets(maKH);
        } else {
            Toast.makeText(this, "Passenger not found", Toast.LENGTH_SHORT).show();
        }
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PassengerDetail.this, passenger_activity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void fetchPassengerDetails(String maKH) {
        ApiService.searchFlight.getPassenger().enqueue(new Callback<ApiResponse<List<Passenger>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Passenger>>> call, Response<ApiResponse<List<Passenger>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Passenger> passengers = response.body().getData(); // Lấy danh sách hành khách
                    Passenger selectedPassenger = null;

                    // Tìm hành khách có maKH tương ứng
                    for (Passenger passenger : passengers) {
                        if (passenger.getMaKH().equals(maKH)) {
                            selectedPassenger = passenger;
                            break;
                        }
                    }

                    if (selectedPassenger != null) {
                        populateUI(selectedPassenger, null); // Hiển thị thông tin hành khách
                    } else {
                        Toast.makeText(PassengerDetail.this, "Hành khách không tìm thấy", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PassengerDetail.this, "Lỗi khi lấy dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Passenger>>> call, Throwable t) {
                Toast.makeText(PassengerDetail.this, "Lỗi API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    // Fetch flight and ticket details by maKH
    private void fetchSumTickets(String maKH) {
        ApiService.searchFlight.getSum(Integer.parseInt(maKH)).enqueue(new Callback<ApiResponse<SumTicket>>() {
            @Override
            public void onResponse(Call<ApiResponse<SumTicket>> call, Response<ApiResponse<SumTicket>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SumTicket sumTicket = response.body().getData(); // Lấy đối tượng thay vì mảng
                    if (sumTicket != null) {
                        populateUI(null, sumTicket); // Cập nhật UI với SumTicket
                    } else {
                        Toast.makeText(PassengerDetail.this, "No ticket data available", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<SumTicket>> call, Throwable t) {
                Toast.makeText(PassengerDetail.this, "API call failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void populateUI(Passenger passenger, SumTicket sumTicket) {
        if (passenger != null) {
            txtHoVaTen.setText(passenger.getFullname());
            txtEmail.setText(passenger.getEmail());
            txtDiaChi.setText(passenger.getDiaChi());
            txtSoDienThoai.setText(passenger.getSoDT());
        }

        if (sumTicket != null) {
            txtChuyenBay.setText(sumTicket.getTongSoChuyenBay());
            txtSoLuong.setText(sumTicket.getTongSoLuongVeDat()); // Sửa ở đây
        } else {
            // Nếu sumTicket null, hiển thị thông báo
            txtChuyenBay.setText("Chưa có chuyến bay");
            txtSoLuong.setText("Chưa đặt vé");
        }
    }

}
