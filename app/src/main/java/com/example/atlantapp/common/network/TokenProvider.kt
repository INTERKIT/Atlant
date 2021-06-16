package com.example.atlantapp.common.network

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.runBlocking

private const val KEY_SESSION_TOKEN = "KEY_SESSION_TOKEN"

class TokenProvider(
    private val sharedPreferences: SharedPreferences
) {

    var token: String = runBlocking { sharedPreferences.getString(KEY_SESSION_TOKEN, "").orEmpty() }
        set(value) {
            field = value
            runBlocking {
                sharedPreferences.edit { putString(KEY_SESSION_TOKEN, value) }
            }
        }

    fun clear() {
        token = ""
    }
}