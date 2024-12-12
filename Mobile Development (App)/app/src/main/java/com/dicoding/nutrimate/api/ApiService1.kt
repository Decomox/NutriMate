package com.dicoding.nutrimate.api

import com.dicoding.nutrimate.response.MainResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiService1 {
    @GET("api/cutting")
    fun getCuttingData(): Call<MainResponse>
    @GET("api/bulking")
    fun getBulkingData(): Call<MainResponse>
    @GET("api/maintaining")
    fun getMaintainingData(): Call<MainResponse>
}

