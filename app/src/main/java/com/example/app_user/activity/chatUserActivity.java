package com.example.app_user.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_user.Adapter.ChatAdapter;
import com.example.app_user.Adapter.FlightAdapter;
import com.example.app_user.R;
import com.example.app_user.inteface.ApiService;
import com.example.app_user.model.Chat;
import com.example.app_user.model.Flight;
import com.example.app_user.model.Mess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class chatUserActivity extends AppCompatActivity {
    private SessionManager sessionManager;
    private CardView cardViewChatUser;
    private List<Chat> mListChat;
    private ChatAdapter chatAdapter;
    ImageView backButtonChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_chat);
        sessionManager = new SessionManager(getApplicationContext());
        backButtonChat = findViewById(R.id.backButtonChat);
        mListChat = new ArrayList<>();
//        cardViewChatUser = findViewById(R.id.cardUserChat);
        backButtonChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(chatUserActivity.this, home_activity.class);
                startActivity(intent);
                finish();
            }
        });
        RecyclerView recyclerView = findViewById(R.id.recyclerViewChat);
        chatAdapter = new ChatAdapter(this, mListChat);

        // Thiết lập LayoutManager cho RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Gắn Adapter vào RecyclerView
        recyclerView.setAdapter(chatAdapter);
        getChatUser();

//
//        cardViewChatUser.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(chatUserActivity.this, chatActivity.class);
//                startActivity(intent);
//                finish();
//            }
//
//        });

    }

    private void getChatUser() {
        ApiService.searchFlight.getListChat(new HashMap<>()).enqueue(new Callback<ApiResponse<List<Chat>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Chat>>> call, Response<ApiResponse<List<Chat>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Chat>> apiResponse = response.body();
                    if (apiResponse.getData() != null) {
                        mListChat = apiResponse.getData();
                        // Cập nhật dữ liệu mới cho Adapter
                        chatAdapter.setData(mListChat);
                        // Thông báo rằng dữ liệu đã thay đổi
                        chatAdapter.notifyDataSetChanged();
                    }
                } else {
                    Log.e("API Error", "Error: " + response.code() + " " + response.message());
                    Toast.makeText(chatUserActivity.this, "Call Api error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Chat>>> call, Throwable t) {
                Log.e("API Error", "Error: " + t.getMessage());
                Toast.makeText(chatUserActivity.this, "Call Api error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}



