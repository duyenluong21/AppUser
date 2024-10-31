package com.example.app_user.Adapter;

import androidx.recyclerview.widget.RecyclerView;

import androidx.annotation.NonNull;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.app_user.R;
import com.example.app_user.activity.PassengerDetail;
import com.example.app_user.model.Passenger;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class PassengerAdapter extends RecyclerView.Adapter<PassengerAdapter.PassengerViewHolder> {
    private List<Passenger> passengers;
    private List<Passenger> passengersFull;
    private Context context;
    public PassengerAdapter(Context context, List<Passenger> passengers) {
        this.context = context; // Gán context
        this.passengers = passengers;
        this.passengersFull = new ArrayList<>(passengers); // Khởi tạo passengersFull
    }
    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
        this.passengersFull = new ArrayList<>(passengers);
        notifyDataSetChanged();
    }
    public PassengerAdapter(List<Passenger> passengers){
        this.passengers = passengers;
    }


    protected PassengerAdapter(Parcel in) {
        passengers = in.createTypedArrayList(Passenger.CREATOR);
    }

    public static final Parcelable.Creator<PassengerAdapter> CREATOR = new Parcelable.Creator<PassengerAdapter>() {
        @Override
        public PassengerAdapter createFromParcel(Parcel in) {
            return new PassengerAdapter(in);
        }

        @Override
        public PassengerAdapter[] newArray(int size) {
            return new PassengerAdapter[size];
        }
    };

    @NonNull
    @Override
    public PassengerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listpassenger,parent,false);
        return new PassengerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PassengerViewHolder holder, int position) {
        Passenger passenger = passengers.get(position);
        holder.txtHoTen.setText(passenger.getFullname());
//        holder.txtGioiTinh.setText(passenger.getGioiTinh());
        holder.txtNgaySinh.setText(passenger.getNgaySinh());
        holder.txtDiaChi.setText(passenger.getDiaChi());
        holder.txtsdt.setText(passenger.getSoDT());
        // Set click listener for the item
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PassengerDetail.class);
            intent.putExtra("maKH", passenger.getMaKH()); // Truyền maKH
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return passengers.size();
    }

    public class PassengerViewHolder extends RecyclerView.ViewHolder{
        TextView txtHoTen, txtNgaySinh, txtDiaChi, txtsdt;
        public PassengerViewHolder(@NonNull View itemView) {
            super(itemView);
            txtHoTen = itemView.findViewById(R.id.txtHoTen);
//            txtGioiTinh = itemView.findViewById(R.id.txtGioiTinh);
            txtNgaySinh = itemView.findViewById(R.id.txtNgaySinh);
            txtDiaChi = itemView.findViewById(R.id.txtDiaChi);
            txtsdt = itemView.findViewById(R.id.txtsdt);
        }
    }
    // Phương thức lọc danh sách khách hàng
    // Phương thức lọc danh sách khách hàng
    // Phương thức lọc danh sách khách hàng
    public void filter(String query) {
        String normalizedQuery = removeDiacritics(query.toLowerCase()); // Chuyển đổi chuỗi tìm kiếm sang không dấu
        if (query.isEmpty()) {
            passengers = new ArrayList<>(passengersFull); // Khôi phục danh sách gốc
        } else {
            List<Passenger> filteredList = new ArrayList<>();
            for (Passenger passenger : passengersFull) {
                // Kiểm tra nếu tên khách hàng hoặc số điện thoại chứa chuỗi tìm kiếm không dấu
                if (removeDiacritics(passenger.getFullname()).toLowerCase().contains(normalizedQuery) ||
                        passenger.getSoDT().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(passenger);
                }
            }
            passengers = filteredList; // Cập nhật danh sách với kết quả lọc
        }
        notifyDataSetChanged(); // Thông báo Adapter rằng dữ liệu đã thay đổi
    }

    // Phương thức chuyển đổi chuỗi có dấu sang không dấu
    private String removeDiacritics(String text) {
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }


}
