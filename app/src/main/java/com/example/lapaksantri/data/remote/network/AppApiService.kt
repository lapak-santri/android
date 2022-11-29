package com.example.lapaksantri.data.remote.network

import com.example.lapaksantri.data.remote.response.SliderResponse
import retrofit2.http.POST

interface AppApiService {
    @POST("slider")
    suspend fun getSliders() : List<SliderResponse>
}