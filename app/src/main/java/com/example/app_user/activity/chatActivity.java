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
import com.example.app_user.model.Mess;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
        if (intent != null) {
            maKH = intent.getStringExtra("maKH");
            maNV = intent.getStringExtra("maNV");

            // Kiểm tra xem maKH và maNV có null không trước khi gọi getListMess
            if (!TextUtils.isEmpty(maKH)) {
                getListMess();
            } else {
                Log.e("YourTag", "maKH is null");
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

    private void getListMess() {
        chatReference.child(maKH).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mListMess.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Mess message = snapshot.getValue(Mess.class);
                    if (message != null) {
                        mListMess.add(message);
                    }
                }
                messAdapter.setData(mListMess);
                messAdapter.notifyDataSetChanged();
                rcvMess.smoothScrollToPosition(mListMess.size() - 1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FirebaseError", "Error: " + databaseError.getMessage());
                Toast.makeText(chatActivity.this, "Error loading messages", Toast.LENGTH_SHORT).show();
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
            if (maKH != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String thoiGianGui = sdf.format(new Date());
                addMess(maKH, strMess, thoiGianGui);
                mListMess.add(new Mess(maKH, strMess, thoiGianGui));
                messAdapter.notifyDataSetChanged();
                rcvMess.scrollToPosition(mListMess.size() - 1);
            } else {
                Log.e("YourTag", "maKH is null");
                Toast.makeText(getApplicationContext(), "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e("YourTag", "EditText or its text is null");
        }
    }

    private void addMess(String maKH, String strMess, String thoiGianGui) {
        String messageId = chatReference.child(maKH).push().getKey();
        if (messageId != null) {
            Mess message = new Mess(maKH, strMess, thoiGianGui);
            chatReference.child(maKH).child(messageId).setValue(message)
                    .addOnSuccessListener(aVoid -> {
                        editMess.setText("");
                        Toast.makeText(getApplicationContext(), "Tin nhắn đã được gửi", Toast.LENGTH_LONG).show();
                    })
                    .addOnFailureListener(e -> {
                        Log.e("FirebaseError", "Error: " + e.getMessage());
                        Toast.makeText(getApplicationContext(), "Không gửi được tin nhắn", Toast.LENGTH_LONG).show();
                    });
        }
    }
}
