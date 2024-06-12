package com.example.app_user.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_user.Adapter.MessAdapter;
import com.example.app_user.R;
import com.example.app_user.inteface.ApiService;
import com.example.app_user.model.Mess;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class chatActivity extends AppCompatActivity {

    private EditText editMess;
    private Button btnSend;
    private RecyclerView rcvMess;
    private MessAdapter messAdapter;
    private List<Mess> mListMess;
    private SessionManager sessionManager;
    ImageView backButton;
    TextView txtNamePassenger;
    Mess messData ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        backButton = findViewById(R.id.backButtonMess);
        editMess = findViewById(R.id.edit_mess);
        btnSend = findViewById(R.id.btn_send);
        rcvMess = findViewById(R.id.rcv_mess);
        sessionManager = new SessionManager(getApplicationContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvMess.setLayoutManager(linearLayoutManager);;
        mListMess = new ArrayList<>();
        messAdapter = new MessAdapter();
        messAdapter.setData(mListMess);
        rcvMess.setAdapter(messAdapter);
        Intent intent = getIntent();
        String maKH = intent.getStringExtra("maKH");
        getListMess(maKH);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(chatActivity.this, chatUserActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMess();
            }
        });

        editMess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkKeyboard();
            }
        });
    }



    private void checkKeyboard() {
        final View activityRootView = findViewById(R.id.activityRoot);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();

                activityRootView.getWindowVisibleDisplayFrame(r);

                int heightDiff = activityRootView.getRootView().getHeight();
                if (heightDiff > 0.25 * activityRootView.getRootView().getHeight()) {
                    if (mListMess.size() > 0) {
                        rcvMess.scrollToPosition(mListMess.size() - 1);
                        activityRootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
            }
        });
    }

    private void getListMess(String maKH) {
        ApiService.searchFlight.getListMess(maKH).enqueue(new Callback<ApiResponse<List<Mess>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Mess>>> call, Response<ApiResponse<List<Mess>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Mess>> apiResponse = response.body();
                    if (apiResponse.getData() != null) {
                        messAdapter = new MessAdapter();
                        rcvMess.setAdapter(messAdapter);
                        mListMess = apiResponse.getData();
                        for (Mess mess : mListMess) {
                            Log.d("MessContent", "maTN: " + mess.getMaTN() + ", noiDung1: " + mess.getNoiDung1());
                            // Thêm các trường khác nếu cần thiết
                        }
                        messAdapter.setData(mListMess);
                        messAdapter.notifyDataSetChanged();
                    }
                } else {
                    Log.e("API Error", "Error: " + response.code() + " " + response.message());
                    Toast.makeText(chatActivity.this, "Call Api error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Mess>>> call, Throwable t) {
                Log.e("API Error", "Error: " + t.getMessage());
                Toast.makeText(chatActivity.this, "Call Api error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void sendMess() {
        if (editMess != null && editMess.getText() != null) {
            String strMess = editMess.getText().toString();
            if (TextUtils.isEmpty(strMess)) {
                Log.d("YourTag", "EditText content is empty");
                return;
            }
            Intent intent = getIntent();
            if (intent != null && intent.hasExtra("maKH")) {
                String maKH = intent.getStringExtra("maKH");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String thoiGianGui = sdf.format(new Date());
                Log.d("YourTag", "maKH in sendMess: " + maKH);

                // Gọi addMess để thêm tin nhắn và gửi lên API
                addMess(maKH, strMess, thoiGianGui);
                mListMess.add(new Mess(maKH,strMess, thoiGianGui));
                messAdapter.notifyDataSetChanged();
                rcvMess.scrollToPosition(mListMess.size() - 1);
            } else {
                Log.e("YourTag", "Intent or maKH is null");
                Toast.makeText(getApplicationContext(), "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
            }

        } else {
            Log.e("YourTag", "EditText or its text is null");
        }
    }

    private void addMess(String maKH, String strMess, String thoiGianGui) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        HashMap<String, String> requestData = new HashMap<>();
        String currentTime = sdf.format(new Date());
        requestData.put("maKH", maKH);
        requestData.put("noiDung1", strMess);
        requestData.put("thoiGianGui", currentTime);
//        requestData.put("noiDung2", "");


        Log.d("YourTag", "maKH in addMess: " + maKH);
        Log.d("YourTag", "requestData in addMess: " + requestData.toString());

        ApiService.searchFlight.storeMess(requestData).enqueue(new Callback<ApiResponse<List<Mess>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Mess>>> call, Response<ApiResponse<List<Mess>>> response) {
                if (response.isSuccessful()) {
                    // Xử lý thành công
                    editMess.setText("");
                    Toast.makeText(getApplicationContext(), "Tin nhắn đã được gửi", Toast.LENGTH_LONG).show();
                } else {
                    // Xử lý lỗi
                    editMess.setText("");
                    Toast.makeText(getApplicationContext(), "Không gửi được tin nhắn", Toast.LENGTH_LONG).show();
                }
            }

            public void onFailure(Call<ApiResponse<List<Mess>>> call, Throwable t) {
                Log.e("YourTag", "Error: " + t.getMessage());

            }
        });
    }

}
