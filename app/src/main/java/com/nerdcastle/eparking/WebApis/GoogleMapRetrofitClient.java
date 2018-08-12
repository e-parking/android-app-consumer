package com.nerdcastle.eparking.WebApis;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GoogleMapRetrofitClient {
    public static Retrofit getRetrofitClient(){
       return new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}
