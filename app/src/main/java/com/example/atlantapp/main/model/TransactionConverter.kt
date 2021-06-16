package com.example.atlantapp.main.model

import com.example.atlantapp.socket.model.Out

object TransactionConverter {

    fun fromNetwork(response: Out): Transaction =
        Transaction(
            address = response.address.orEmpty(),
            value = response.value.toBigInteger()
        )
}