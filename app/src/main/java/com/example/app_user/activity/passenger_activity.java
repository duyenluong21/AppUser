package com.example.app_user.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Debug;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_user.Adapter.PassengerAdapter;
import com.example.app_user.R;
import com.example.app_user.inteface.ApiService;
import com.example.app_user.model.Passenger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class passenger_activity extends AppCompatActivity {
    private List<Passenger> passengers;
    private SessionManager sessionManager;
    private PassengerAdapter passengerAdapter; // Đảm bảo biến này được khai báo
    ImageView backButton;
    SearchView timKiemKH;
    private static final String TRACE_FILE_NAME = "appUserTrace.trace";
    private static final int REQUEST_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger);
        backButton = findViewById(R.id.backButton);
        timKiemKH = findViewById(R.id.timKiemKH);
        sessionManager = new SessionManager(getApplicationContext());
        Log.v("Logger verbose","Ứng dụng đang khởi chạy");
        Log.d("Debug message", "Đang kiểm tra trạng thái ứng dụng.");
        Log.i("Info message"," Ứng dụng khởi chạy thành công.");
        Log.w("Warning message"," Không tìm thấy dữ liệu nhưng ứng dụng vẫn tiếp tục.");
        Log.e("Error message" ,"Lỗi kết nối với máy chủ.");
        Log.wtf("WTF message", "Hệ thống gặp lỗi không thể khắc phục.");

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(passenger_activity.this, home_activity.class);
            startActivity(intent);
            finish();
        });

        // Khởi tạo RecyclerView và Adapter
        RecyclerView recyclerView = findViewById(R.id.recyclerViewBookedTickets);
        // Khởi tạo passengerAdapter và gán cho biến passengerAdapter
        passengerAdapter = new PassengerAdapter(this, new ArrayList<>()); // Thay đổi ở đây

        // Thiết lập LayoutManager cho RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Gắn Adapter vào RecyclerView
        recyclerView.setAdapter(passengerAdapter); // Đảm bảo rằng passengerAdapter đã được khởi tạo

        // Gọi API để lấy danh sách chuyến bay
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Yêu cầu quyền truy cập
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
        } else {
            // Được phép, thực hiện tracing
            startMethodTracing();
        }


        // Thiết lập SearchView để tìm kiếm khách hàng
        timKiemKH.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Khi người dùng nhập văn bản
                passengerAdapter.filter(newText); // Gọi phương thức lọc trong Adapter
                return true;
            }
        });
    }

    private void startMethodTracing() {
        // Kiểm tra nếu quyền đã được cấp và bắt đầu tracing
        String traceFile = getExternalFilesDir(null) + "/appUserTrace.trace"; // Tùy chỉnh đường dẫn file
//        Debug.startMethodTracing(traceFile);
//        Debug.startMethodTracing(traceFile, 8 * 1024);
        Debug.startMethodTracing(traceFile, 16 * 1024, Debug.TRACE_COUNT_ALLOCS);
        // Tiến hành công việc bạn muốn đo hiệu suất
        getListPassenger();
        Log.d("Path trace file: ", traceFile);
        Debug.stopMethodTracing(); // Dừng tracing sau khi công việc hoàn thành
    }

    private void getListPassenger() {
        // Ghi lại thời gian bắt đầu
        long startTimeMillis = System.currentTimeMillis();
        long startTimeNano = System.nanoTime();
        long startElapsedRealtime = SystemClock.elapsedRealtime();
        long startUptimeMillis = SystemClock.uptimeMillis();

        ApiService.searchFlight.getListUser(new HashMap<>()).enqueue(new Callback<ApiResponse<List<Passenger>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Passenger>>> call, Response<ApiResponse<List<Passenger>>> response) {
                // Ghi lại thời gian kết thúc
                long endTimeMillis = System.currentTimeMillis();
                long endTimeNano = System.nanoTime();
                long endElapsedRealtime = SystemClock.elapsedRealtime();
                long endUptimeMillis = SystemClock.uptimeMillis();

                // Tính toán thời gian thực hiện
                Log.d("API Timing", "Time in millis: " + (endTimeMillis - startTimeMillis) + " ms");
                Log.d("API Timing", "Time in nano: " + (endTimeNano - startTimeNano) + " ns");
                Log.d("API Timing", "Elapsed realtime: " + (endElapsedRealtime - startElapsedRealtime) + " ms");
                Log.d("API Timing", "Uptime millis: " + (endUptimeMillis - startUptimeMillis) + " ms");

                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Passenger>> apiResponse = response.body();
                    if (apiResponse.getData() != null) {
                        passengers = apiResponse.getData();
                        // Cập nhật dữ liệu mới cho Adapter
                        passengerAdapter.setPassengers(passengers); // Gọi setPassengers từ passengerAdapter
                    }
                } else {
                    Log.e("API Error", "Error: " + response.code() + " " + response.message());
                    Toast.makeText(passenger_activity.this, "Call Api error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Passenger>>> call, Throwable t) {
                // Ghi lại thời gian kết thúc trong trường hợp lỗi
                long endTimeMillis = System.currentTimeMillis();
                long endTimeNano = System.nanoTime();
                long endElapsedRealtime = SystemClock.elapsedRealtime();
                long endUptimeMillis = SystemClock.uptimeMillis();

                // Tính toán thời gian thực hiện
                Log.d("API Timing", "Time in millis: " + (endTimeMillis - startTimeMillis) + " ms");
                Log.d("API Timing", "Time in nano: " + (endTimeNano - startTimeNano) + " ns");
                Log.d("API Timing", "Elapsed realtime: " + (endElapsedRealtime - startElapsedRealtime) + " ms");
                Log.d("API Timing", "Uptime millis: " + (endUptimeMillis - startUptimeMillis) + " ms");

                Log.e("API Error", "Error: " + t.getMessage());
                Toast.makeText(passenger_activity.this, "Call Api error", Toast.LENGTH_SHORT).show();
            }
        });
    }


//    private void getListPassenger() {
//        ApiService.searchFlight.getListUser(new HashMap<>()).enqueue(new Callback<ApiResponse<List<Passenger>>>() {
//            @Override
//            public void onResponse(Call<ApiResponse<List<Passenger>>> call, Response<ApiResponse<List<Passenger>>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    ApiResponse<List<Passenger>> apiResponse = response.body();
//                    if (apiResponse.getData() != null) {
//                        passengers = apiResponse.getData();
//                        // Cập nhật dữ liệu mới cho Adapter
//                        passengerAdapter.setPassengers(passengers); // Gọi setPassengers từ passengerAdapter
//                    }
//                } else {
//                    Log.e("API Error", "Error: " + response.code() + " " + response.message());
//                    Toast.makeText(passenger_activity.this, "Call Api error", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ApiResponse<List<Passenger>>> call, Throwable t) {
//                Log.e("API Error", "Error: " + t.getMessage());
//                Toast.makeText(passenger_activity.this, "Call Api error", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}