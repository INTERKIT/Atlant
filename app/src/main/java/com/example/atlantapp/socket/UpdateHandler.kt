package com.example.atlantapp.socket

import org.json.JSONObject

interface UpdateHandler {
    suspend fun initialize()
    suspend fun onUpdate(type: UpdateType, data: JSONObject)
}