package com.example.lapaksantri.data.remote.network

import com.example.lapaksantri.data.remote.request.LoginRequest
import com.example.lapaksantri.data.remote.request.RegisterRequest
import com.example.lapaksantri.data.remote.response.LoginResponse
import com.example.lapaksantri.data.remote.response.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ) : LoginResponse
    @POST("register")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ) : RegisterResponse
}