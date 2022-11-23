package com.example.lapaksantri.data.remote.response


import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("name")
    val name: String
)