package com.example.lapaksantri.data.remote.response


import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("token")
    val token: String,
    @SerializedName("name")
    val name: String
)