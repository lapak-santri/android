package com.example.lapaksantri.data.remote.network

import com.example.lapaksantri.data.remote.response.address.GetAddressesResponse
import retrofit2.http.GET
import retrofit2.http.Header

interface AddressApiService {
    @GET("address")
    suspend fun getAddresses(
        @Header("Authorization") token: String,
    ): GetAddressesResponse
}