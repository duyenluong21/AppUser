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

import com.example.app_user.Adapter.ChatAdapter;
import com.example.app_user.R;
import com.example.app_user.model.Chat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class chatUserActivity extends AppCompatActivity {
    private List<Chat> mListChat;
    private ChatAdapter chatAdapter;
    private ImageView backButtonChat;
    private DatabaseReference chatReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_chat);

        backButtonChat = findViewById(R.id.backButtonChat);
        mListChat = new ArrayList<>();

        // Firebase reference to "Chats" node
        chatReference = FirebaseDatabase.getInstance().getReference("Chats");

        // Set up back button to navigate to home activity
        backButtonChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(chatUserActivity.this, home_activity.class);
                startActivity(intent);
                finish();
            }
        });

        // Set up RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewChat);
        chatAdapter = new ChatAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatAdapter);

        // Load chat data from Firebase
        getChatUser();
    }

    private void getChatUser() {
        // Listening for changes in "Chats" node in Firebase
        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mListChat.clear(); // Clear the list to avoid duplication
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class); // Parse each chat object
                    if (chat != null) {
                        mListChat.add(chat); // Add chat to the list
                    }
                }
//                chatAdapter.setData(mListChat); // Update adapter with new data
                chatAdapter.notifyDataSetChanged(); // Notify adapter about data changes
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FirebaseError", "Error: " + databaseError.getMessage());
                Toast.makeText(chatUserActivity.this, "Error loading chat data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
