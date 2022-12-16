package com.example.lapaksantri.data.remote.network

import com.example.lapaksantri.data.remote.request.AddUpdateAddressRequest
import com.example.lapaksantri.data.remote.response.address.AddAddressResponse
import com.example.lapaksantri.data.remote.response.address.GetAddressesResponse
import com.example.lapaksantri.data.remote.response.address.UpdateAddressResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AddressApiService {
    @GET("address")
    suspend fun getAddresses(
        @Header("Authorization") token: String,
    ): GetAddressesResponse

    @POST("address")
    suspend fun addAddress(
        @Header("Authorization") token: String,
        @Body addUpdateAddressRequest: AddUpdateAddressRequest
    ): AddAddressResponse

    @PUT("address/{id}")
    suspend fun updateAddress(
        @Header("Authorization") token: String,
        @Path("id") addressId: Int,
        @Body addUpdateAddressRequest: AddUpdateAddressRequest,
    ): UpdateAddressResponse

    @DELETE("address/{id}")
    suspend fun deleteAddress(
        @Header("Authorization") token: String,
        @Path("id") addressId: Int,
    ): UpdateAddressResponse
}