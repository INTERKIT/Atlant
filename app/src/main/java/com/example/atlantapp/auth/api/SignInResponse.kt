package com.example.atlantapp.auth.api

import com.google.gson.annotations.SerializedName

data class SignInResponse(
    @SerializedName("token")
    val token: String,
    @SerializedName("expiration")
    val expiration: Long,
    @SerializedName("serverTime")
    val serverTime: Long
)