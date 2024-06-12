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
import com.example.app_user.model.Mess;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private List<Chat> mListChat;
    private Context mContext;

    public ChatAdapter(Context context, List<Chat> listChat) {
        mContext = context;
        mListChat = listChat;
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
            holder.txtNamePassenger.setText(chat.getFullname());

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
    public void setData(List<Chat> listChat) {
        mListChat = listChat;
        notifyDataSetChanged();
    }
    public class ChatViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNamePassenger;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNamePassenger = itemView.findViewById(R.id.txtNamePassenger);
        }
    }
}
