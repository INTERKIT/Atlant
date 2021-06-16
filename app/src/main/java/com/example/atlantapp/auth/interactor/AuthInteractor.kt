package com.example.atlantapp.auth.interactor

import com.example.atlantapp.auth.repository.AuthRepository
import com.example.atlantapp.common.network.TokenProvider
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

class AuthInteractor(
    private val sessionId: String,
    private val authRepository: AuthRepository,
    private val tokenProvider: TokenProvider
) {

    fun isRegistered() = tokenProvider.token.isNotEmpty()

    suspend fun signIn(email: String, password: String) {
        val token = authRepository.signIn(email, password)
        tokenProvider.token = token
    }

    suspend fun refreshToken(): String {
        val token = authRepository.refreshToken()
        tokenProvider.token = token
        return token
    }

    fun logout() {
        tokenProvider.clear()

        GlobalScope.launch {
            try {
                authRepository.logout(sessionId)
            } catch (e: Throwable) {
                Timber.w(e, "Error sending logout request")
            }
        }
    }
}