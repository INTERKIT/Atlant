package com.example.atlantapp.main.api

import retrofit2.http.GET

interface MainApi {

    @GET("accounts/current")
    suspend fun getProfile(): ProfileWrapperResponse
}