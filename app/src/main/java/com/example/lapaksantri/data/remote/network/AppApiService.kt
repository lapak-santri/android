package com.example.lapaksantri.data.remote.network

import com.example.lapaksantri.data.remote.response.GetSlidersResponse
import retrofit2.http.GET

interface AppApiService {
    @GET("slider")
    suspend fun getSliders() : GetSlidersResponse
}