package com.example.lapaksantri.data.remote.network

import com.example.lapaksantri.data.remote.request.AddCartRequest
import com.example.lapaksantri.data.remote.request.AddTransactionRequest
import com.example.lapaksantri.data.remote.request.UpdateCartRequest
import com.example.lapaksantri.data.remote.response.cart.AddUpdateCartResponse
import com.example.lapaksantri.data.remote.response.cart.GetCartsResponse
import com.example.lapaksantri.data.remote.response.order.AddTransactionResponse
import com.example.lapaksantri.data.remote.response.order.GetTransactionsResponse
import com.example.lapaksantri.data.remote.response.product.GetProductsResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface OrderApiService {
    @GET("product")
    suspend fun getProduct(): GetProductsResponse

    @GET("cart")
    suspend fun getCart(
        @Header("Authorization") token: String,
    ): GetCartsResponse

    @POST("cart")
    suspend fun addCarts(
        @Header("Authorization") token: String,
        @Body addCartRequest: List<AddCartRequest>,
    ): AddUpdateCartResponse

    @PUT("cart")
    suspend fun updateCart(
        @Header("Authorization") token: String,
        @Body updateCartRequest: UpdateCartRequest,
    ): AddUpdateCartResponse

    @DELETE("cart/{id}")
    suspend fun deleteCart(
        @Header("Authorization") token: String,
        @Path("id") cartId: Int
    ): AddUpdateCartResponse

    @POST("transaction")
    suspend fun addTransaction(
        @Header("Authorization") token: String,
        @Body addTransactionRequest: AddTransactionRequest
    ): AddTransactionResponse

    @GET("transaction")
    suspend fun getTransactions(
        @Header("Authorization") token: String,
    ): GetTransactionsResponse
}