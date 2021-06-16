package com.example.atlantapp.main.model

sealed class ProfileResult {
    data class Success(val data: Profile) : ProfileResult()
    object Failed : ProfileResult()
}