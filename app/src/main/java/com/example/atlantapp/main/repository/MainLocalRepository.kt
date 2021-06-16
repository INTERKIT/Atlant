package com.example.atlantapp.main.repository

import com.example.atlantapp.main.model.Transaction
import kotlinx.coroutines.flow.Flow

interface MainLocalRepository {
    suspend fun addTransaction(transaction: Transaction)
    fun getTransactionsFLow(): Flow<List<Transaction>>
    fun clear()
}