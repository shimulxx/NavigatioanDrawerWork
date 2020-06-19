package com.example.navigtiondrawerwork.Models;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitClient {
    @POST("posts")
    Call<Orders> gotoFakePayment(@Body Orders orders);
}
