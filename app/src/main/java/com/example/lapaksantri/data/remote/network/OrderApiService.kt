package com.example.lapaksantri.data.remote.network

import com.example.lapaksantri.data.remote.response.product.GetProductsResponse
import retrofit2.http.GET

interface OrderApiService {
    @GET("product")
    suspend fun getProduct(): GetProductsResponse
}