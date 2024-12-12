package com.dicoding.nutrimate.data

import com.google.gson.annotations.SerializedName

data class IsiRequest(
    @SerializedName("weight") val weight: Int,
    @SerializedName("height") val height: Int,
    @SerializedName("gender") val gender: String,
    @SerializedName("age") val age: Int,
    @SerializedName("activityLevel") val activityLevel: String
)
