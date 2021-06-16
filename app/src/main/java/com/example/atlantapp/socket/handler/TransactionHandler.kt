package com.example.atlantapp.socket.handler

import com.example.atlantapp.main.interactor.MainInteractor
import com.example.atlantapp.main.model.TransactionConverter
import com.example.atlantapp.socket.UpdateHandler
import com.example.atlantapp.socket.UpdateType
import com.example.atlantapp.socket.model.TransactionResponse
import com.google.gson.Gson
import org.json.JSONObject

class TransactionHandler(
    private val gson: Gson,
    private val mainInteractor: MainInteractor
) : UpdateHandler {

    override suspend fun initialize() {
        // nothing to initialize for transactions
    }

    override suspend fun onUpdate(type: UpdateType, data: JSONObject) {
        if (type != UpdateType.TRANSACTION) return

        /* FIXME: probably response.x.out.firstOrNull is wrong, workaround */
        val response = gson.fromJson(data.toString(), TransactionResponse::class.java)
        val out = response.x.out.firstOrNull() ?: return

        val transaction = TransactionConverter.fromNetwork(out)
        mainInteractor.addTransaction(transaction)
    }
}