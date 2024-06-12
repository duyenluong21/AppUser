package com.example.app_user.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_user.Adapter.FlightAdapter;
import com.example.app_user.R;
import com.example.app_user.activity.ApiResponse;
import com.example.app_user.inteface.ApiService;
import com.example.app_user.model.Flight;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlightFragment extends Fragment {
    private RecyclerView recyclerViewNotFlight;
    private ApiService searchFlight;
    private List<com.example.app_user.model.Flight> flightNotBooked = new ArrayList<>();
    private FlightAdapter flightNotBookedAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragement_flight, container, false);

        recyclerViewNotFlight = view.findViewById(R.id.recyclerViewBookedTickets);
        flightNotBookedAdapter = new FlightAdapter(flightNotBooked);
        recyclerViewNotFlight.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewNotFlight.setAdapter(flightNotBookedAdapter);

        searchFlight = ApiService.searchFlight;
        getUserBookedTickets();
        return view;
    }

    private void getUserBookedTickets() {
        Map<String, String> options = new HashMap<>();
        searchFlight.getListFlights().enqueue(new Callback<ApiResponse<List<Flight>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Flight>>> call, Response<ApiResponse<List<Flight>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Flight> notBookedTickets = response.body().getData();
                    List<Flight> userBookedTickets = ApiService.filterBookedTicketsByMaKH(notBookedTickets);

                    flightNotBooked.clear();
                    for (Flight flight : userBookedTickets) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date ticketDate = null;
                        try {
                            ticketDate = dateFormat.parse(flight.getNgayDi());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if (ticketDate != null) {
                            Date currentDate = new Date();
                            if (ticketDate.after(currentDate)) {
                                flightNotBooked.add(flight);
                            }
                        }
                    }

                    // Thay đổi dữ liệu trong adapter và thông báo cập nhật
                    flightNotBookedAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), "Failed to retrieve booked tickets data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Flight>>> call, Throwable t) {
                Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}