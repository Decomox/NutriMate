package com.dicoding.nutrimate.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig2 {

    private const val BASE_URL = "https://nutrimatelogin-427962917963.asia-southeast2.run.app/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService2 by lazy {
        retrofit.create(ApiService2::class.java)
    }
}
