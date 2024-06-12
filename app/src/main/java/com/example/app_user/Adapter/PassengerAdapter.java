package com.example.app_user.Adapter;

import androidx.recyclerview.widget.RecyclerView;

import com.example.app_user.activity.MainActivity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.app_user.R;
import com.example.app_user.model.Flight;
import com.example.app_user.model.passenger;

import java.util.ArrayList;
import java.util.List;

public class PassengerAdapter extends RecyclerView.Adapter<PassengerAdapter.PassengerViewHolder> {
    private List<passenger> passengers;
    public PassengerAdapter() {
        this.passengers = new ArrayList<>();
    }
    public void setPassengers(List<passenger> passengers) {
        this.passengers = passengers;
        notifyDataSetChanged();
    }
    public PassengerAdapter(List<passenger> passengers){
        this.passengers = passengers;
    }


    protected PassengerAdapter(Parcel in) {
        passengers = in.createTypedArrayList(passenger.CREATOR);
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
        passenger passenger = passengers.get(position);
        holder.txtHoTen.setText(passenger.getFullname());
        holder.txtGioiTinh.setText(passenger.getGioiTinh());
        holder.txtNgaySinh.setText(passenger.getNgaySinh());
        holder.txtDiaChi.setText(passenger.getDiaChi());
        holder.txtsdt.setText(passenger.getSoDT());
    }

    @Override
    public int getItemCount() {
        return passengers.size();
    }

    public class PassengerViewHolder extends RecyclerView.ViewHolder{
        TextView txtHoTen, txtGioiTinh, txtNgaySinh, txtDiaChi, txtsdt;
        public PassengerViewHolder(@NonNull View itemView) {
            super(itemView);
            txtHoTen = itemView.findViewById(R.id.txtHoTen);
            txtGioiTinh = itemView.findViewById(R.id.txtGioiTinh);
            txtNgaySinh = itemView.findViewById(R.id.txtNgaySinh);
            txtDiaChi = itemView.findViewById(R.id.txtDiaChi);
            txtsdt = itemView.findViewById(R.id.txtsdt);
        }
    }
}
