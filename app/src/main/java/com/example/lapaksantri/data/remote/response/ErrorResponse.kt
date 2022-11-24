package com.example.lapaksantri.data.remote.response


import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("error")
    val errorMessageResponse: ErrorMessageResponse
)