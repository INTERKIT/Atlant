package com.example.atlantapp.main.repository

import com.example.atlantapp.main.model.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class MainInMemoryRepository : MainLocalRepository {

    private val transactionsFlow = MutableStateFlow<List<Transaction>>(emptyList())

    override suspend fun addTransaction(transaction: Transaction) {
        val transactions = transactionsFlow.value.toMutableList()
        transactions.add(transaction)
        transactionsFlow.emit(transactions)
    }

    override fun getTransactionsFlow(): Flow<List<Transaction>> = transactionsFlow

    override fun clear() {
        transactionsFlow.value = emptyList()
    }
}