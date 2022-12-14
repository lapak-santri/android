package com.example.lapaksantri.data.repositories

import com.example.lapaksantri.data.remote.network.OrderApiService
import com.example.lapaksantri.data.remote.request.AddCartRequest
import com.example.lapaksantri.data.remote.request.UpdateCartRequest
import com.example.lapaksantri.data.remote.response.ErrorResponse
import com.example.lapaksantri.domain.entities.Article
import com.example.lapaksantri.domain.entities.Cart
import com.example.lapaksantri.domain.entities.Product
import com.example.lapaksantri.domain.repositories.OrderRepository
import com.example.lapaksantri.utils.Resource
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val orderApiService: OrderApiService,
    private val dataStoreManager: DataStoreManager
) : OrderRepository {
    override fun getProducts(): Flow<Resource<List<Product>>> = flow {
        emit(Resource.Loading())
        try {
           val response = orderApiService.getProduct()
            emit(Resource.Success(response.dataProductResponse.data.map {
                Product(
                    id = it.id,
                    name = it.name,
                    price = it.price.toDouble(),
                    stock = it.quantity,
                    imagePath = it.image[0],
                )
            }))
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

    override fun getCarts(): Flow<Resource<List<Cart>>> = flow {
        emit(Resource.Loading())
        try {
            val token = dataStoreManager.token.first()
            if (token != "") {
                val response = orderApiService.getCart(
                    token = "Bearer $token"
                )
                emit(Resource.Success(response.dataCartResponse.data.map {
                    Cart(
                        id = it.product.id,
                        name = it.product.name,
                        price = it.product.price.toDouble(),
                        imagePath = it.product.image[0],
                        quantity = it.quantity,
                    )
                }))
            } else {
                emit(Resource.Error("Token Not Exist"))
            }
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

    override fun addCarts(carts: List<Cart>): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val token = dataStoreManager.token.first()
            if (token != "") {
                emit(Resource.Success("Success"))
            } else {
                emit(Resource.Error("Token Not Exist"))
            }
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

    override fun updateCarts(cartId: Int, type: String): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val token = dataStoreManager.token.first()
            if (token != "") {
                orderApiService.updateCart(
                    token = "Bearer $token",
                    updateCartRequest = UpdateCartRequest(
                        idProduct = cartId,
                        quantity = 1,
                        type = type
                    )
                )
                emit(Resource.Success("Success"))
            } else {
                emit(Resource.Error("Token Not Exist"))
            }
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

    override fun deleteCarts(cartId: Int): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val token = dataStoreManager.token.first()
            if (token != "") {
                emit(Resource.Success("Success"))
            } else {
                emit(Resource.Error("Token Not Exist"))
            }
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

    override fun addCarts(carts: List<Cart>): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val token = dataStoreManager.token.first()
            if (token != "") {
                orderApiService.addCarts(
                    token = "Bearer $token",
                    cartRequest = carts.map {
                        AddCartRequest(
                            idProduct = it.id,
                            quantity = it.quantity,
                        )
                    }
                )
                emit(Resource.Success("Add Carts Successfully"))
            } else {
                emit(Resource.Error("Token Not Exist"))
            }
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