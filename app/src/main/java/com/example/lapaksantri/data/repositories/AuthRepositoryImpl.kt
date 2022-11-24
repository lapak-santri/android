package com.example.lapaksantri.data.repositories

import com.example.lapaksantri.data.local.data_store.DataStoreManager
import com.example.lapaksantri.data.remote.network.AuthApiService
import com.example.lapaksantri.data.remote.request.LoginRequest
import com.example.lapaksantri.domain.repositories.AuthRepository
import com.example.lapaksantri.utils.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class AuthRepositoryImpl(
    private val authApiService: AuthApiService,
    private val dataStoreManager: DataStoreManager
) : AuthRepository {
    override fun login(email: String, password: String): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val response = authApiService.login(
                LoginRequest(
                    email = email,
                    password = password
                )
            )
            dataStoreManager.saveName(response.name)
            dataStoreManager.saveToken(response.token)
            emit(Resource.Success("Login Successful"))
        } catch (e: Exception) {
            when(e) {
                is HttpException -> {
                    val errorMessageResponseType = object : TypeToken<ErrorResponse>() {}.type
                    val error: ErrorResponse = Gson().fromJson(e.response()?.errorBody()?.charStream(), errorMessageResponseType)
                    emit(Resource.Error(error.errorMessageResponse.message))
                }
                else -> {
                    emit(Resource.Error("An unexpected error occurred"))
                }
            }
        }
    }

    override fun checkToken(): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading())
        try {
            delay(1000)
            val token = dataStoreManager.token.first()
            if (token != "") {
                emit(Resource.Success(true))
            } else {
                emit(Resource.Error("Token Not Exist", false))
            }
        } catch (e: Exception) {
            emit(Resource.Error("An unexpected error occurred", false))
        }
    }
}