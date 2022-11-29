package com.example.lapaksantri.data.remote.response


import com.google.gson.annotations.SerializedName

data class GetUserResponse(
    @SerializedName("data")
    val userResponse: UserResponse,
    @SerializedName("status")
    val status: String
)