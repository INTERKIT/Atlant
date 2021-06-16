package com.example.atlantapp.socket.model

import com.google.gson.annotations.SerializedName

data class SocketRequest(
    @SerializedName("op")
    val operation: String
)