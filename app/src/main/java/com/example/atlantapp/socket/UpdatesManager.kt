package com.example.atlantapp.socket

interface UpdatesManager {
    fun start()

    /**
     * Stops updates manager. This action is terminal and updates manager can't be restarted after it.
     */
    fun stop()
}