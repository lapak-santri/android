package com.example.lapaksantri.data.repositories

import com.example.lapaksantri.data.local.data_store.DataStoreManager
import com.example.lapaksantri.data.remote.network.AuthApiService
import com.example.lapaksantri.data.remote.request.LoginRequest
import com.example.lapaksantri.data.remote.request.RegisterRequest
import com.example.lapaksantri.data.remote.response.ErrorResponse
import com.example.lapaksantri.domain.entities.User
import com.example.lapaksantri.domain.repositories.AuthRepository
import com.example.lapaksantri.utils.Resource
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

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

    override fun register(name: String, email: String, password: String): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val response = authApiService.register(
                RegisterRequest(
                    name = name,
                    email = email,
                    password = password
                )
            )
            emit(Resource.Success(response.message))
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

    override fun getName(): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val name = dataStoreManager.name.first()
            if (name != "") {
                emit(Resource.Success(name))
            } else {
                emit(Resource.Error("Token Not Exist"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("An unexpected error occurred"))
        }
    }

    override fun getUser(): Flow<Resource<User>> = flow {
        emit(Resource.Loading())
        try {
            val token = dataStoreManager.token.first()
            if (token != "") {
                val result = authApiService.getUser("Bearer $token")
                emit(Resource.Success(result.userResponse.toEntity()))
            } else {
                emit(Resource.Error("Token Not Exist"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }

    override fun signOut(): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading())
        try {
            dataStoreManager.saveToken("")
            dataStoreManager.saveName("")
            emit(Resource.Success(true))
        } catch (e: Exception) {
            emit(Resource.Error("An unexpected error occurred", false))
        }
    }
}