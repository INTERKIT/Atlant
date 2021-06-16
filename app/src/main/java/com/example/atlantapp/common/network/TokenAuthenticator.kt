package com.example.atlantapp.common.network

import com.example.atlantapp.auth.interactor.AuthInteractor
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import org.koin.core.KoinComponent
import org.koin.core.inject

private val mutex = Mutex()

class TokenAuthenticator : Authenticator, KoinComponent {

    private val authInteractor: AuthInteractor by inject()
    private val tokenProvider: TokenProvider by inject()

    override fun authenticate(route: Route?, response: Response): Request? {
        val request = response.request
        return refreshToken(request, retryCount(request) + 1)
    }

    private fun refreshToken(request: Request, retryCount: Int): Request? = runBlocking {
        if (retryCount > 2) return@runBlocking null

        mutex.withLock {
            val lastSavedToken = tokenProvider.token
            val currentToken = request.header("Authorization")

            if (currentToken != null && lastSavedToken != currentToken) {
                /* Access token is refreshed, retrying last rejected request with updated token */
                return@withLock retryRejectedRequest(request, lastSavedToken)
            }

            // Refresh your access_token using a synchronous api request
            val token = authInteractor.refreshToken()
            retryRejectedRequest(request, token)
        }
    }

    /*
    * Count the number of retries to prevent excessive numbers of refresh token calls
    * */
    private fun retryCount(request: Request): Int =
        request.header("RetryCount")?.toInt() ?: 0

    private fun retryRejectedRequest(request: Request, accessToken: String): Request =
        request.newBuilder()
            .header("Authorization", accessToken)
            .build()
}