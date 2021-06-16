package com.example.atlantapp.common.network

import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor(private val tokenProvider: TokenProvider) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val token = tokenProvider.token

        val newRequest = request.newBuilder().apply {
            if (token.isNotEmpty()) addHeader("Authorization",  token)
        }

        return chain.proceed(newRequest.build())
    }
}