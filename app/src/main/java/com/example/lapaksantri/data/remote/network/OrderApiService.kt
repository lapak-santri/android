package com.example.lapaksantri.data.remote.network

import com.example.lapaksantri.data.remote.request.AddCartRequest
import com.example.lapaksantri.data.remote.response.order.AddCartsResponse
import com.example.lapaksantri.data.remote.response.product.GetProductsResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface OrderApiService {
    @GET("product")
    suspend fun getProduct(): GetProductsResponse

    @POST("cart")
    suspend fun addCarts(
        @Header("Authorization") token: String,
        @Body cartRequest: List<AddCartRequest>
    ): AddCartsResponse
}