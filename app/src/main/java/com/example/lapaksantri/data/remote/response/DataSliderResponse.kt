package com.example.lapaksantri.data.remote.response


import com.google.gson.annotations.SerializedName

data class DataSliderResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("data")
    val `data`: List<SliderResponse>
)