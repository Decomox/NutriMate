package com.dicoding.nutrimate.data

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)