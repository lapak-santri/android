package com.example.lapaksantri.data.remote.response.slider


import com.google.gson.annotations.SerializedName

data class GetSlidersResponse(
    @SerializedName("data")
    val dataSliderResponse: DataSliderResponse,
    @SerializedName("status")
    val status: String
)