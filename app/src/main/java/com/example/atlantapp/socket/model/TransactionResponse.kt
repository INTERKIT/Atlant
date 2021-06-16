package com.example.atlantapp.socket.model

import com.google.gson.annotations.SerializedName

data class TransactionResponse(
    @SerializedName("x")
    val x: X
)

data class X(
    @SerializedName("out")
    val out: List<Out>
)

data class Out(
    @SerializedName("addr")
    val address: String?,
    @SerializedName("value")
    val value: Long
)