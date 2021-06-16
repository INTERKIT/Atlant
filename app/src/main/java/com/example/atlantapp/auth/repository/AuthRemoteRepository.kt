package com.example.atlantapp.auth.repository

import com.example.atlantapp.auth.api.AuthApi
import com.example.atlantapp.auth.api.LogoutRequest
import com.example.atlantapp.auth.api.SignInRequest

class AuthRemoteRepository(
    private val api: AuthApi
) : AuthRepository {

    override suspend fun signIn(email: String, password: String): String {
        val request = SignInRequest(email, password)
        return api.signIn(request).token
    }

    override suspend fun refreshToken(): String = api.refreshToken().token

    override suspend fun logout(sessionId: String) {
        val request = LogoutRequest(sessionId)
        api.logout(request)
    }
}