package com.example.atlantapp.main.repository

import com.example.atlantapp.main.model.ProfileResult

interface MainRepository {
    suspend fun getProfile(): ProfileResult
}