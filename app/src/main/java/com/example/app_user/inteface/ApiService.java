package com.example.app_user.inteface;


import com.example.app_user.model.Chat;
import com.example.app_user.model.Flight;
import com.example.app_user.model.Mess;
import com.example.app_user.model.SumTicket;
import com.example.app_user.model.Ticket;
import com.example.app_user.model.User;
import com.example.app_user.model.Passenger;
import com.example.app_user.activity.ApiResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiService {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    ApiService searchFlight = new Retrofit.Builder()
            .baseUrl("http://192.168.1.4/TTCS/app/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);
    @GET("api/readFlights.php")
    Call<ApiResponse<List<Flight>>> getListFlights();

    @GET("api/readPassenger.php")
    Call<ApiResponse<List<Passenger>>> getListUser(@QueryMap Map<String,String> option);

    @GET("api/readTicket.php")
    Call<ApiResponse<List<Ticket>>> getListTicket(@QueryMap Map<String,String> option);

    @POST("api/createMess.php")
    Call<ApiResponse<List<Mess>>> storeMess(@Body Map<String, String> requestData);
    @GET("api/readMess.php")
    Call<ApiResponse<List<Mess>>> getListMess(@Query("maKH") String maKH);
    @GET("api/readChat.php")
    Call<ApiResponse<List<Chat>>> getListChat(@QueryMap Map<String,String> option);
    @GET("api/readUser.php")
    Call<ApiResponse<List<User>>> getUser();
    @GET("api/readPassenger.php")
    Call<ApiResponse<List<Passenger>>> getPassenger();
    @GET("api/readSumTicket.php")
    Call<ApiResponse<SumTicket>> getSum(@Query("maKH") int maKH);


    @GET("api/readUser.php")
    Call<ApiResponse<User>> getUserByMaNV(@Query("maNV") String maNV);
    static List<Flight> filterBookedTicketsByMaKH(List<Flight> allBookedTickets) {
        List<Flight> userBookedTickets = new ArrayList<>();

        for (Flight flight : allBookedTickets) {
                userBookedTickets.add(flight);
        }

        return userBookedTickets;
    }
}

