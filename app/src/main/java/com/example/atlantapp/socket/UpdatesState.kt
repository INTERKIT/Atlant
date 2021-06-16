package com.example.atlantapp.socket

enum class UpdatesState {
    DISCONNECTED,
    INITIALIZING,
    INITIALIZATION_FAILED,
    CONNECTING,
    CONNECTING_FAILED,
    CONNECTED
}