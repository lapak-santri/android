package com.example.lapaksantri.data.remote.response


import com.google.gson.annotations.SerializedName

data class ErrorMessageResponse(
    @SerializedName("message")
    val message: String
)