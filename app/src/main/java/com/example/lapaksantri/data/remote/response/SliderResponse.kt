package com.example.lapaksantri.data.remote.response

import com.google.gson.annotations.SerializedName
import com.example.lapaksantri.domain.entities.Slider

data class SliderResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val imageResponse: String,
) {
    fun toEntity(): Slider = Slider(id, imageResponse)
}