package com.example.app_user.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_user.R;
import com.example.app_user.activity.chatActivity;
import com.example.app_user.model.Chat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private List<Chat> mListChat;
    private Context mContext;

    public ChatAdapter(Context context) {
        mContext = context;
        mListChat = new ArrayList<>();
        loadChats(); // Gọi phương thức để tải dữ liệu chat từ Firebase
    }

    private void loadChats() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mListChat.clear();
                Set<String> addedCustomers = new HashSet<>(); // Tập hợp để lưu các mã KH đã xử lý

                for (DataSnapshot chatSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot messageSnapshot : chatSnapshot.getChildren()) {
                        Chat chat = messageSnapshot.getValue(Chat.class);
                        if (chat != null && !addedCustomers.contains(chat.getMaKH())) {
                            // Chỉ thêm vào danh sách nếu maKH chưa tồn tại trong addedCustomers
                            mListChat.add(chat);
                            addedCustomers.add(chat.getMaKH()); // Đánh dấu maKH đã thêm vào danh sách
                        }
                    }
                }
                notifyDataSetChanged(); // Cập nhật adapter sau khi tải dữ liệu
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FirebaseError", databaseError.getMessage());
            }
        });
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list_chat_user, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Chat chat = mListChat.get(position);
        if (chat != null) {
            holder.txtNamePassenger.setText(chat.getFullname() != null ? chat.getFullname() : "Người dùng không xác định");

            // Lấy mã KH từ mục tương ứng
            final String maKH = chat.getMaKH();

            // Thiết lập sự kiện nhấp vào mục
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Chuyển sang chatActivity và truyền mã KH
                    Intent intent = new Intent(mContext, chatActivity.class);
                    intent.putExtra("maKH", maKH);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mListChat != null ? mListChat.size() : 0;
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNamePassenger;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNamePassenger = itemView.findViewById(R.id.txtNamePassenger);
        }
    }
}
