package com.example.lapaksantri.data.remote.response.order


import com.google.gson.annotations.SerializedName

data class AddTransactionResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("transaction")
    val transactionResponse: TransactionResponse
)