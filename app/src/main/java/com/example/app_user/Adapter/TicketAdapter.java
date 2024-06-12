package com.example.app_user.Adapter;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_user.model.Ticket;

import java.util.ArrayList;
import java.util.List;
import com.example.app_user.R;
import com.example.app_user.model.passenger;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {
    private List<Ticket> tickets;
    public TicketAdapter() {
        this.tickets = new ArrayList<>();
    }
    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
        notifyDataSetChanged();
    }
    public TicketAdapter(List<Ticket> tickets){
        this.tickets = tickets;
    }


    protected TicketAdapter(Parcel in) {
        tickets = in.createTypedArrayList(Ticket.CREATOR);
    }

    public static final Parcelable.Creator<TicketAdapter> CREATOR = new Parcelable.Creator<TicketAdapter>() {
        @Override
        public TicketAdapter createFromParcel(Parcel in) {
            return new TicketAdapter(in);
        }

        @Override
        public TicketAdapter[] newArray(int size) {
            return new TicketAdapter[size];
        }
    };


    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_ticket,parent,false);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        Ticket ticket = tickets.get(position);
        holder.txtLoaiVe.setText(ticket.getLoaiVe());
        holder.txtSoLuong.setText(ticket.getSoLuong());

    }


    @Override
    public int getItemCount() {
        return tickets.size();
    }

    public class TicketViewHolder extends RecyclerView.ViewHolder{
        TextView txtLoaiVe, txtSoLuong;
        public TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            txtLoaiVe = itemView.findViewById(R.id.txtLoaiVe);
            txtSoLuong = itemView.findViewById(R.id.txtSoLuongCon);
        }
    }
}
