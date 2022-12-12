package com.example.lapaksantri.data.remote.network

import com.example.lapaksantri.data.remote.response.article.GetArticlesResponse
import com.example.lapaksantri.data.remote.response.slider.GetSlidersResponse
import retrofit2.http.GET

interface AppApiService {
    @GET("slider")
    suspend fun getSliders(): GetSlidersResponse
    @GET("article")
    suspend fun getArticles(): GetArticlesResponse
}