package com.example.lapaksantri.data.remote.response.article


import com.google.gson.annotations.SerializedName

data class DataArticleResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("data")
    val `data`: List<ArticleResponse>
)