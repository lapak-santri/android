package com.example.lapaksantri.data.remote.response.article


import com.google.gson.annotations.SerializedName

data class GetArticlesResponse(
    @SerializedName("data")
    val dataArticleResponse: DataArticleResponse,
    @SerializedName("status")
    val status: String
)