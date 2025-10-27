package com.example.pachacaj.myapplication;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    /*@POST("")
    Call<>
    */


    @GET("login")
    Call<LoginResponse> getLogin(
            @Query("username") String username,
            @Query("password") String password
    );

}
