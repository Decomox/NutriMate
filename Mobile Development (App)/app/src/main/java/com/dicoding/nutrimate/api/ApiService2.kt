package com.dicoding.nutrimate.api

import com.dicoding.nutrimate.data.IsiRequest
import com.dicoding.nutrimate.data.LoginRequest
import com.dicoding.nutrimate.data.ProgressRequest
import com.dicoding.nutrimate.data.RegisterRequest
import com.dicoding.nutrimate.response.LoginResponse
import com.dicoding.nutrimate.response.ProgressResponse
import com.dicoding.nutrimate.response.RegisterResponse
import com.dicoding.nutrimate.response.RekomResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService2 {
    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<RegisterResponse>

    @POST("api/progress")
    fun updateProgress(
        @Body progressRequest: ProgressRequest
    ): Call<ProgressResponse>

    @POST("api/recommendation")
    suspend fun rekomendasiProgram(
        @Body request: IsiRequest
    ): Response<RekomResponse>

}
