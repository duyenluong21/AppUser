package com.example.app_user.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
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
import com.example.app_user.Crypto.AESKeyStorage;
import com.example.app_user.Crypto.AESUtil;
import com.example.app_user.Crypto.KeyStoreHelper;
import com.example.app_user.Crypto.RSAUtil;
import com.example.app_user.R;
import com.example.app_user.inteface.ApiService;
import com.example.app_user.model.Mess;
import com.example.app_user.model.PublicKeyPassenger;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.crypto.SecretKey;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class chatActivity extends AppCompatActivity {

    private EditText editMess;
    private Button btnSend;
    private RecyclerView rcvMess;
    private MessAdapter messAdapter;
    private List<Mess> mListMess;
    private DatabaseReference chatReference;
    private String maKH;
    private String maNV;
    ImageView backButton;
    TextView txtNamePassenger;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        backButton = findViewById(R.id.backButtonMess);
        editMess = findViewById(R.id.edit_mess);
        btnSend = findViewById(R.id.btn_send);
        rcvMess = findViewById(R.id.rcv_mess);

        chatReference = FirebaseDatabase.getInstance().getReference("Chats");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvMess.setLayoutManager(linearLayoutManager);
        mListMess = new ArrayList<>();
        messAdapter = new MessAdapter();
        messAdapter.setData(mListMess);
        rcvMess.setAdapter(messAdapter);

        Intent intent = getIntent();
        String fullname = intent.getStringExtra("fullname");
        txtNamePassenger = findViewById(R.id.tvNamePassenger);
        txtNamePassenger.setText(fullname);
        if (intent != null) {
            maKH = intent.getStringExtra("maKH");
            SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
            maNV = sharedPreferences.getString("maNV", null);

            if (!TextUtils.isEmpty(maKH) && !TextUtils.isEmpty(maNV)) {
                getListMess(this, maKH, maNV);
            } else {
                Log.e("YourTag", "maKH is null, maNV is null");
            }
        }

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
                sendMessage(v.getContext());
            }
        });

        editMess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkKeyboard();
            }
        });
    }

    private String getChatId(String maKH, String maNV) {
        return maKH + "_" + maNV;
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

    private void getListMess(Context context, String maKH, String maNV) {
        String chatId = getChatId(maKH, maNV);

        DatabaseReference chatReference = FirebaseDatabase.getInstance().getReference("Chats");
        chatReference.child(chatId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mListMess.clear();
                int totalMessages = 0;
                int successfullyDecrypted = 0;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Mess message = snapshot.getValue(Mess.class);
                    totalMessages++;

                    if (message != null) {
                        try {
                            if (message.getEncryptedMessage() == null) {
                                Log.e("DecryptionError", "Encrypted message is null for message ID: " + snapshot.getKey());
                                continue;
                            }

                            String decryptedMessage = null;

                            if (!message.isFromCustomer()) {
                                // Nếu tin nhắn từ khách hàng, dùng khóa AES từ AESKeyStorage
                                String aesKeyAlias = maKH + "_" + maNV;
                                SecretKey aesKey = AESKeyStorage.getAESKey(context, aesKeyAlias);
                                if (aesKey == null) {
                                    Log.e("DecryptionError", "AES Key not found for alias: " + aesKeyAlias);
                                    continue;
                                }

                                decryptedMessage = AESUtil.decrypt(message.getEncryptedMessage(), aesKey);
                            } else {
                                // Nếu tin nhắn từ nhân viên, dùng private key để giải mã AES key
                                if (message.getEncryptedAESKey() == null) {
                                    Log.e("DecryptionError", "Encrypted AES Key is null for message ID: " + snapshot.getKey());
                                    continue;
                                }

                                PrivateKey privateKey = KeyStoreHelper.getPrivateKey(maNV);
                                if (privateKey == null) {
                                    Log.e("DecryptionError", "Private Key not found in KeyStore for maKH: " + maKH);
                                    continue;
                                }

                                // Giải mã AES Key
                                String decryptedAESKeyString = RSAUtil.decryptWithPrivateKey(message.getEncryptedAESKey(), privateKey);
                                if (TextUtils.isEmpty(decryptedAESKeyString)) {
                                    Log.e("DecryptionError", "Failed to decrypt AES Key for message ID: " + snapshot.getKey());
                                    continue;
                                }

                                SecretKey aesKey = AESUtil.convertStringToAESKey(decryptedAESKeyString);
                                if (aesKey == null) {
                                    Log.e("DecryptionError", "Failed to convert decrypted AES Key to SecretKey for message ID: " + snapshot.getKey());
                                    continue;
                                }
                                if (aesKey == null) {
                                    Log.e("DecryptionError", "Failed to decrypt AES Key for message ID: " + snapshot.getKey());
                                    continue;
                                }

                                // Giải mã nội dung tin nhắn bằng AES Key
                                decryptedMessage = AESUtil.decrypt(message.getEncryptedMessage(), aesKey);
                            }

                            if (!TextUtils.isEmpty(decryptedMessage)) {
                                message.setDecryptedMessage(decryptedMessage);
                                mListMess.add(message);
                                successfullyDecrypted++;
                            } else {
                                Log.e("DecryptionError", "Decrypted message is empty for message ID: " + snapshot.getKey());
                            }

                        } catch (Exception e) {
                            Log.e("DecryptionError", "Error decrypting message for ID: " + snapshot.getKey() + ", error: " + e.getMessage(), e);
                        }
                    }
                }
                Log.d("DecryptionStats", "Total messages processed: " + totalMessages);
                Log.d("DecryptionStats", "Successfully decrypted messages: " + successfullyDecrypted);

                // Cập nhật giao diện
                messAdapter.setData(mListMess);
                messAdapter.notifyDataSetChanged();
                if (!mListMess.isEmpty()) {
                    rcvMess.smoothScrollToPosition(mListMess.size() - 1);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("DatabaseError", "Failed to read messages: " + databaseError.getMessage());
            }
        });
    }

    private void sendMessage(Context context) {
        try {
            String messageContent = editMess != null ? editMess.getText().toString() : null;
            if (TextUtils.isEmpty(messageContent)) {
                Log.e("SendMessage", "Message content is empty or EditText is null.");
                return;
            }
            String recipientAlias = maKH + "_" + maNV;
            if (TextUtils.isEmpty(recipientAlias)) {
                Log.e("SendMessage", "Recipient alias is invalid.");
                return;
            }
            fetchPublicKeyFromServer(maKH, new PublicKeyCallback() {
                @Override
                public void onSuccess(String publicKeyString) {
                    try {
                        // Bước 4: Lấy hoặc tạo khóa AES dùng chung
                        SecretKey aesKey = AESKeyStorage.getAESKey(context, recipientAlias);
                        if (aesKey == null) {
                            aesKey = AESUtil.generateAESKey(256);
                            AESKeyStorage.saveAESKey(context, recipientAlias, aesKey);
                        }

                        // Bước 5: Mã hóa nội dung tin nhắn bằng AES
                        String encryptedMessage = AESUtil.encrypt(messageContent, aesKey);
                        if (TextUtils.isEmpty(encryptedMessage)) {
                            Log.e("SendMessage", "Encrypted message is empty.");
                            return;
                        }

                        // Bước 6: Mã hóa khóa AES bằng Public Key của người nhận
                        PublicKey recipientPublicKey = RSAUtil.getPublicKeyFromString(publicKeyString);
                        byte[] aesKeyBytes = aesKey.getEncoded();
                        if (aesKeyBytes == null || aesKeyBytes.length == 0) {
                            Log.e("SendMessage", "AES key encoding returned null or empty.");
                            return;
                        }

                        String encryptedAESKey = RSAUtil.encryptWithPublicKey(
                                Base64.encodeToString(aesKeyBytes, Base64.NO_WRAP),
                                recipientPublicKey
                        );
                        if (TextUtils.isEmpty(encryptedAESKey)) {
                            Log.e("SendMessage", "Encrypted AES key is empty.");
                            return;
                        }

                        // Bước 7: Chuẩn bị thời gian gửi
                        String sendTime = getCurrentTime();
                        if (TextUtils.isEmpty(sendTime)) {
                            Log.e("SendMessage", "Send time is invalid.");
                            return;
                        }

                        // Bước 8: Gửi tin nhắn và khóa AES đã mã hóa
                        addEncryptedMess(maKH, maNV, encryptedAESKey, sendTime, encryptedMessage);
                        Log.d("SendMessage", "Message sent successfully!");

                        // Xóa nội dung tin nhắn trong EditText sau khi gửi
                        if (editMess != null) {
                            editMess.setText("");
                        }

                    } catch (Exception e) {
                        Log.e("SendMessage", "Error processing message: " + e.getMessage(), e);
                    }
                }

                @Override
                public void onFailure(String errorMessage) {
                    Log.e("SendMessage", "Failed to fetch Public Key: " + errorMessage);
                }
            });

        } catch (Exception e) {
            Log.e("SendMessage", "Error sending message: " + e.getMessage(), e);
        }
    }

    private void addEncryptedMess(String maKH, String maNV, String encryptedAESKey, String sendTime, String encryptedMessage) {
        String chatId = getChatId(maKH, maNV);

        DatabaseReference chatReference = FirebaseDatabase.getInstance().getReference("Chats");
        String messageId = chatReference.child(chatId).push().getKey();
        if (messageId != null) {
            Mess mess = new Mess(maKH, maNV, null, encryptedAESKey, sendTime, encryptedMessage, false);
            chatReference.child(chatId).child(messageId).setValue(mess)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("SendMess", "Message saved successfully.");
                        } else {
                            Log.e("SendMess", "Failed to save message: " + task.getException().getMessage());
                        }
                    });
        } else {
            Log.e("SendMess", "Failed to generate message ID.");
        }
    }

    private void fetchPublicKeyFromServer(String maKH, final PublicKeyCallback callback) {
        ApiService.searchFlight.getPublicKey(maKH).enqueue(new Callback<ApiResponse<PublicKeyPassenger>>() {
            @Override
            public void onResponse(Call<ApiResponse<PublicKeyPassenger>> call, Response<ApiResponse<PublicKeyPassenger>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<PublicKeyPassenger> apiResponse = response.body();

                    if (apiResponse != null && apiResponse.getData() != null) {
                        PublicKeyPassenger publicKeyRequest = apiResponse.getData();
                        String publicKeyString = publicKeyRequest.getPublic_key(); // Lấy chuỗi public key

                        // Trả lại publicKeyString qua callback
                        callback.onSuccess(publicKeyString);
                    }
                } else {
                    callback.onFailure("Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<PublicKeyPassenger>> call, Throwable t) {
                callback.onFailure("Failed to fetch public key: " + t.getMessage());
            }
        });
    }

    public interface PublicKeyCallback {
        void onSuccess(String publicKeyString);
        void onFailure(String errorMessage);
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}
