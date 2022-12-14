package com.example.lapaksantri.data.remote.network

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

    @GET("cart")
    suspend fun getCart(
        @Header("Authorization") token: String,
    ): GetCartsResponse

    @POST("cart")
    suspend fun addCart(
        @Header("Authorization") token: String,
        @Body addCartRequest: AddCartRequest,
    ): AddUpdateCartResponse

    @PUT("cart")
    suspend fun updateCart(
        @Header("Authorization") token: String,
        @Body updateCartRequest: UpdateCartRequest,
    ): AddUpdateCartResponse
}