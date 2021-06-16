package com.example.atlantapp.main.model

data class Profile(
    val firstName: String,
    val lastName: String,
    val type: String
) {

    val fullName: String
        get() = "$firstName $lastName"
}