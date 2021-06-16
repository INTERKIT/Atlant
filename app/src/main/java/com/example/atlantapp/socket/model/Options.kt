package com.example.atlantapp.socket.model

class Options(
    val url: String,
    val origin: String = "https://exchange.blockchain.com",

    /**
     * Not used in this project
     * */
    val apiKey: String
)