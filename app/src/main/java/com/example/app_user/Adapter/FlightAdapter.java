package com.example.app_user.Adapter;

import androidx.recyclerview.widget.RecyclerView;

import com.example.app_user.activity.MainActivity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.app_user.R;
import com.example.app_user.model.Flight;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class FlightAdapter extends RecyclerView.Adapter<FlightAdapter.FlightViewHolder> implements Parcelable{
    private List<Flight> flights;
    public FlightAdapter() {
        this.flights = new ArrayList<>();
    }
    public void setFlights(List<Flight> flights) {
        this.flights = flights;
        notifyDataSetChanged();
    }
    public FlightAdapter(List<Flight> flights){
        this.flights = flights;
    }


    protected FlightAdapter(Parcel in) {
        flights = in.createTypedArrayList(Flight.CREATOR);
    }

    public static final Creator<FlightAdapter> CREATOR = new Creator<FlightAdapter>() {
        @Override
        public FlightAdapter createFromParcel(Parcel in) {
            return new FlightAdapter(in);
        }

        @Override
        public FlightAdapter[] newArray(int size) {
            return new FlightAdapter[size];
        }
    };

    @NonNull
    @Override
    public FlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new FlightViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlightViewHolder holder, int position) {
        Flight flight = flights.get(position);

        // Gán dữ liệu từ đối tượng Flight vào các thành phần View
        holder.txtmaCB.setText(flight.getMaCB());
        holder.txtNgayDi.setText(flight.getNgayDi());
        holder.txtNgayDen.setText(flight.getNgayDen());
        holder.txtDiaDiemDi.setText(flight.getDiaDiemDi());
        holder.txtDiaDiemDen.setText(flight.getDiaDiemDen());
        holder.txtGioBay.setText(flight.getGioBay());

    }



    @Override
    public int getItemCount() {
        return flights.size();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeTypedList(flights);
    }

    public class FlightViewHolder extends RecyclerView.ViewHolder {
        TextView txtmaCB, txtNgayDi, txtNgayDen, txtDiaDiemDi, txtDiaDiemDen, txtGioBay;

        public FlightViewHolder(@NonNull View itemView) {
            super(itemView);
            txtmaCB = itemView.findViewById(R.id.txtmaCB);
            txtNgayDi = itemView.findViewById(R.id.txtNgayDi);
            txtNgayDen = itemView.findViewById(R.id.txtNgayDen);
            txtDiaDiemDi = itemView.findViewById(R.id.txtDiaDiemDi);
            txtDiaDiemDen = itemView.findViewById(R.id.txtDiaDiemDen);
            txtGioBay = itemView.findViewById(R.id.txtGioBay);

        }
    }
}
