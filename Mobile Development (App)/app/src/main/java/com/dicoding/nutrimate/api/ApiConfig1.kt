package com.dicoding.nutrimate.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig1 {
    private const val BASE_URL = "https://modelmealsworkouts-427962917963.asia-southeast2.run.app/"
    val apiService: ApiService1 by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService1::class.java)
    }
}

