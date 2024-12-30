package com.example.app_user.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_user.Crypto.KeyStoreHelper;
import com.example.app_user.inteface.ApiService;
import com.example.app_user.R;
import com.example.app_user.inteface.PublicKeyCheckCallback;
import com.example.app_user.model.PublicKeyRequest;
import com.example.app_user.model.User;

import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

import java.io.Serializable;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
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

    private String convertPublicKeyToBase64(PublicKey publicKey) {
        return Base64.encodeToString(publicKey.getEncoded(), Base64.DEFAULT);
    }

    private void uploadPublicKeyToServer(String maNV, PublicKeyRequest publicKeyRequest) {

        // Gọi API để cập nhật public key
        ApiService.searchFlight.updatePublicKey(maNV, publicKeyRequest)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Log.d("API_SUCCESS", "Cập nhật khóa thành công với maNV: " + maNV);
                            Log.d("DEBUG_publicKey", publicKeyRequest.getPublic_key());
                        } else {
                            Toast.makeText(login.this, "Cập nhật khóa thất bại!", Toast.LENGTH_SHORT).show();
                            Log.e("API_ERROR", "Code: " + response.code() + " Message: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(login.this, "Không thể kết nối tới server!", Toast.LENGTH_SHORT).show();
                        Log.e("API_FAILURE", "Error: " + t.getMessage());
                    }
                });
    }

    private void clickLogin() {
        String strUsername = editUsername.getText().toString().trim();
        String strPassword = editPassword.getText().toString().trim();
        if (strUsername.isEmpty() || strPassword.isEmpty()) {
            Toast.makeText(login.this, "Vui lòng nhập đầy đủ email và mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean isHasUser = false;

        for (User user : mListUser) {
            if (strUsername.equals(user.getUsername()) && strPassword.equals(user.getPassw())) {
                isHasUser = true;
                mUser = user;
                break;
            }
        }

        if (isHasUser) {
            String maNV = mUser.getMaNV();
            checkIfUserHasPublicKey(maNV, hasPublicKey -> {
                if (!hasPublicKey) {
                    try {
                        // Tạo và lưu trữ RSA key pair vào Keystore
                        KeyStoreHelper.generateAndStoreRSAKeyPair(maNV);

                        // Lấy public key từ Keystore và gửi lên server
                        PublicKey publicKey = KeyStoreHelper.getPublicKey(maNV);
                        if (publicKey != null) {
                            String publicKeyBase64 = convertPublicKeyToBase64(publicKey);
                            uploadPublicKeyToServer(maNV, new PublicKeyRequest(publicKeyBase64));
                            Log.e("KeyStore", "Đã lưu được khóa lên csdl");
                        } else {
                            Log.e("KeyStore", "Không thể lấy public key từ Keystore");
                            Toast.makeText(login.this, "Lỗi xử lý khóa bảo mật", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } catch (Exception e) {
                        Log.e("KeyStore", "Lỗi khi tạo hoặc lưu khóa RSA", e);
                        Toast.makeText(login.this, "Lỗi khi xử lý khóa bảo mật", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                // Kiểm tra xem private key đã được lưu chưa
                if (KeyStoreHelper.isPrivateKeyStored(maNV)) {
                    Log.d("KeyStore", "Private key đã được lưu trữ.");
                } else {
                    Log.d("KeyStore", "Private key chưa được lưu trữ.");
                }

                // Lưu thông tin người dùng vào sessionManager
                sessionManager.saveLoginDetails(mUser.getUsername(), mUser.getMaNV(), mUser.getHoNV(), mUser.getTenNV(), mUser.getNgaySinhNV(),
                        mUser.getSdtNV(), mUser.getChucVu(), mUser.getTrinhDoHocVan(), mUser.getKinhNghiem());
                sessionManager.setLogin(true);
                sessionManager.saveUser(mUser);
                SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("maNV", maNV);
                editor.apply();
                Intent intent = new Intent(login.this, home_activity.class);
                intent.putExtra("object_user", mUser);
                Toast.makeText(login.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
            });
        } else {
            Toast.makeText(login.this, "Tài khoản hoặc mật khẩu không chính xác", Toast.LENGTH_SHORT).show();
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

    private void checkIfUserHasPublicKey(String maNV, PublicKeyCheckCallback callback) {
        ApiService.searchFlight.checkIfUserHasPublicKey(maNV)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        // Kiểm tra xem response có thành công không
                        if (response.isSuccessful()) {
                            try {
                                // Parse JSON response để lấy giá trị hasPublicKey
                                JSONObject responseObject = new JSONObject(response.body().string());
                                boolean hasPublicKey = responseObject.getBoolean("hasPublicKey");

                                // Gọi callback để xử lý kết quả
                                callback.onPublicKeyChecked(hasPublicKey);
                            } catch (Exception e) {
                                // Nếu có lỗi trong quá trình parse, coi như chưa có public key
                                callback.onPublicKeyChecked(false);
                            }
                        } else {
                            // Nếu API không thành công, coi như khách hàng chưa có public key
                            callback.onPublicKeyChecked(false);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        // Nếu có lỗi kết nối, coi như khách hàng chưa có public key
                        callback.onPublicKeyChecked(false);
                    }
                });
    }

}
