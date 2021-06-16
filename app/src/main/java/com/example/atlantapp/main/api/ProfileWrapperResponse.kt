package com.example.atlantapp.main.api

import com.google.gson.annotations.SerializedName

data class ProfileWrapperResponse(
    @SerializedName("info")
    val info: InfoResponse
)

data class InfoResponse(
    @SerializedName("profiles")
    val profiles: List<ProfileResponse>
)

data class ProfileResponse(
    @SerializedName("profile_id")
    val profileId: String,
    @SerializedName("account_id")
    val accountId: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("profile_type")
    val profileType: String
)
