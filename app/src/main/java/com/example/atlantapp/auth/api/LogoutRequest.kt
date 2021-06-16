package com.example.atlantapp.auth.api

import com.google.gson.annotations.SerializedName

data class LogoutRequest(
    @SerializedName("session_id")
    val sessionId: String
)