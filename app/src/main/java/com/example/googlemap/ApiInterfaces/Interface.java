package com.example.googlemap.ApiInterfaces;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface Interface {


    @GET("api/place/nearbysearch/json?")
    Call<ResponseBody> GetNearByPlaces
            (

                    @Query("location") String LatLong,
                    @Query("radius") String radius,
                    @Query("keyword") String keyword,
                    @Query("key") String key
            );





}
