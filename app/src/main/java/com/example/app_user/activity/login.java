package com.example.app_user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_user.inteface.ApiService;
import com.example.app_user.R;
import com.example.app_user.model.User;

import org.mindrot.jbcrypt.BCrypt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class login extends AppCompatActivity {
    private SessionManager sessionManager;
    private List<User> mListUser;
    EditText editUsername, editPassword;
    Button buttonLogin;
    private User mUser ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editUsername = findViewById(R.id.username);
        editPassword = findViewById(R.id.password);
        buttonLogin = findViewById(R.id.buttonLogin);
        sessionManager = new SessionManager(getApplicationContext());
        mListUser = new ArrayList<>();
        getListUser();
        buttonLogin.setOnClickListener(v -> {
            clickLogin();
        });
    }

    private void clickLogin() {

        String strUsername = editUsername.getText().toString().trim();
        String strPassword = editPassword.getText().toString().trim();
        if (strUsername.isEmpty() || strPassword.isEmpty()) {
            Toast.makeText(login.this, "Vui lòng nhập đầy đủ email và mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean isHasUser = false ;

        for (User user : mListUser){
            if(strUsername.equals(user.getUsername()) && strPassword.equals(user.getPassw())){
                isHasUser = true ;
                mUser = user ;
                break;
            }
        }
        if(isHasUser){
            sessionManager.saveLoginDetails(mUser.getUsername(), mUser.getMaNV(), mUser.getHoNV(), mUser.getTenNV(),mUser.getNgaySinhNV(),
                    mUser.getSdtNV(), mUser.getChucVu(), mUser.getTrinhDoHocVan(), mUser.getKinhNghiem());
            sessionManager.setLogin(true);
            sessionManager.saveUser(mUser);
            Intent intent = new Intent(login.this, home_activity.class);
            intent.putExtra("object_user", mUser);
            Toast.makeText(login.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
            startActivity(intent);
            finish();
        }

    }

    private void getListUser() {
        ApiService.searchFlight.getUser()
                .enqueue(new Callback<ApiResponse<List<User>>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<List<User>>> call, Response<ApiResponse<List<User>>> response) {
                        ApiResponse<List<User>> apiResponse = response.body();
                        if (apiResponse != null && apiResponse.getData() != null) {
                            mListUser = apiResponse.getData();
                            Log.e("List User:", mListUser.size() + "");
                        } else {
                            // Handle the case where ApiResponse or its data is null
                            Log.e("List User:", "ApiResponse or its data is null");
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<List<User>>> call, Throwable t) {

                    }
                });{

        }
    }
}
