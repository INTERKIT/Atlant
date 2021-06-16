package com.example.atlantapp.main.model

import com.example.atlantapp.main.api.ProfileResponse

object ProfileConverter {

    fun fromNetwork(response: ProfileResponse) : Profile =
        Profile(
            firstName = response.firstName,
            lastName = response.lastName,
            type = response.profileType
        )
}