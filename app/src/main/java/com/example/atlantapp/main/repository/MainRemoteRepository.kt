package com.example.atlantapp.main.repository

import com.example.atlantapp.main.api.MainApi
import com.example.atlantapp.main.model.ProfileConverter
import com.example.atlantapp.main.model.ProfileResult

class MainRemoteRepository(
    private val api: MainApi
) : MainRepository {

    override suspend fun getProfile(): ProfileResult {
        val response = api.getProfile()
        val profileResponse = response.info.profiles.firstOrNull()
        return if (profileResponse != null) {
            val result = ProfileConverter.fromNetwork(profileResponse)
            ProfileResult.Success(result)
        } else {
            ProfileResult.Failed
        }
    }
}