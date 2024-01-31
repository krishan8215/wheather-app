package com.example.wheatherapp2

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface myinterface {

    @GET("weather")
    fun getwheatherdata(
@Query("q")city:String,
@Query("appid")appid:String,
@Query("units")units:String
    ):Call<wheatherapp>



}