package com.example.atlantapp.main.interactor

import com.example.atlantapp.main.model.ProfileResult
import com.example.atlantapp.main.model.Transaction
import com.example.atlantapp.main.repository.MainLocalRepository
import com.example.atlantapp.main.repository.MainRepository
import kotlinx.coroutines.flow.Flow

class MainInteractor(
    private val mainRepository: MainRepository,
    private val mainLocalRepository: MainLocalRepository
) {

    suspend fun getProfile(): ProfileResult =
        mainRepository.getProfile()

    suspend fun addTransaction(transaction: Transaction) {
        mainLocalRepository.addTransaction(transaction)
    }

    fun getTransactionsFLow(): Flow<List<Transaction>> = mainLocalRepository.getTransactionsFLow()

    fun clearMemory() {
        mainLocalRepository.clear()
    }
}