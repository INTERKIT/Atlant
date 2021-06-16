package com.example.atlantapp.auth.api

import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("accounts/auth")
    suspend fun signIn(@Body request: SignInRequest): SignInResponse

    @POST("accounts/sessions/refresh")
    suspend fun refreshToken(): SignInResponse

    @POST("accounts/sessions/end")
    suspend fun logout(@Body request: LogoutRequest)
}