package com.example.atlantapp.common.network

import okhttp3.Interceptor
import okhttp3.Response

class WebSocketInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val newRequest = request.newBuilder().apply {
            addHeader("prod", "Origin: https://exchange.blockchain.com")
        }

        return chain.proceed(newRequest.build())
    }
}