package com.example.lapaksantri.data.repositories

import com.example.lapaksantri.data.remote.network.OrderApiService
import com.example.lapaksantri.data.remote.response.ErrorResponse
import com.example.lapaksantri.domain.entities.Article
import com.example.lapaksantri.domain.entities.Product
import com.example.lapaksantri.domain.repositories.OrderRepository
import com.example.lapaksantri.utils.Resource
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val orderApiService: OrderApiService
) : OrderRepository {
    override fun getProducts(): Flow<Resource<List<Product>>> = flow {
        emit(Resource.Loading())
        try {
            val listProducts = listOf(
                Product(
                    1,
                    "Botol",
                    10000.0,
                    50,
                    "dus",
                    "",
                ),
                Product(
                    2,
                    "Botol",
                    10000.0,
                    50,
                    "dus",
                    "",
                ),
                Product(
                    3,
                    "Botol",
                    10000.0,
                    50,
                    "dus",
                    "",
                ),
            )
            emit(Resource.Success(listProducts))
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
}