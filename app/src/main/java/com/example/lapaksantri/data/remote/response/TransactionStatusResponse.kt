package com.example.lapaksantri.data.remote.response


import com.google.gson.annotations.SerializedName

data class TransactionStatusResponse(
    @SerializedName("transaction_status")
    val status: String?,
    @SerializedName("status_message")
    val statusMessage: String,
    @SerializedName("transaction_id")
    val id: String,
    @SerializedName("status_code")
    val statusCode: String,
)