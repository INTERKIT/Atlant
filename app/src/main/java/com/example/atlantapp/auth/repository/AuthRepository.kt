package com.example.atlantapp.auth.repository

interface AuthRepository {
    suspend fun signIn(email: String, password: String): String
    suspend fun refreshToken(): String
    suspend fun logout(sessionId: String)
}