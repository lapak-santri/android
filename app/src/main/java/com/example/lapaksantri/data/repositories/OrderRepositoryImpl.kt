package com.example.lapaksantri.data.repositories

import android.util.Log
import com.example.lapaksantri.data.local.data_store.DataStoreManager
import com.example.lapaksantri.data.remote.network.OrderApiService
import com.example.lapaksantri.data.remote.request.AddCartRequest
import com.example.lapaksantri.data.remote.request.AddTransactionRequest
import com.example.lapaksantri.data.remote.request.DetailRequest
import com.example.lapaksantri.data.remote.request.UpdateCartRequest
import com.example.lapaksantri.data.remote.response.ErrorResponse
import com.example.lapaksantri.data.remote.response.TransactionStatusResponse
import com.example.lapaksantri.domain.entities.Address
import com.example.lapaksantri.domain.entities.Cart
import com.example.lapaksantri.domain.entities.Product
import com.example.lapaksantri.domain.entities.Transaction
import com.example.lapaksantri.domain.repositories.OrderRepository
import com.example.lapaksantri.utils.Resource
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
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
                val carts = arrayListOf<Cart>()
                response.dataCartResponse.data.forEach { cartResponse ->
                    val product = cartResponse.product
                    val index = carts.indices.find { product.id == carts[it].id }
                    if (index == -1 || index == null) {
                        carts.add(
                            Cart(
                                id = product.id,
                                name = product.name,
                                price = product.price.toDouble(),
                                imagePath = product.image[0],
                                quantity = cartResponse.quantity,
                                cartId = arrayListOf(cartResponse.id)
                            )
                        )
                    } else {
                        carts[index].quantity += cartResponse.quantity
                        carts[index].cartId.add(cartResponse.id)
                    }
                }
                emit(Resource.Success(carts))
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

    override fun deleteCarts(cartId: List<Int>): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val token = dataStoreManager.token.first()
            if (token != "") {
                cartId.forEach {
                    orderApiService.deleteCart(
                        token = "Bearer $token",
                        cartId = it
                    )
                }
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
                    addCartRequest = carts.map {
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

    override fun addTransactions(
        name: String,
        email: String,
        address: Address,
        totalPrice: Int
    ): Flow<Resource<Transaction>> = flow {
        emit(Resource.Loading())
        try {
            val token = dataStoreManager.token.first()
            if (token != "") {
                val detailRequests = arrayListOf<DetailRequest>()
                val carts = orderApiService.getCart(
                    token = "Bearer $token"
                )
                carts.dataCartResponse.data.forEach {
                    detailRequests.add(
                        DetailRequest(
                            it.id,
                            it.idProduct,
                            it.quantity
                        )
                    )
                }
                val transactionCode = SimpleDateFormat(
                    "yyyyMMddHHmmss",
                    Locale("in", "ID"),
                ).format(Date())
                val response = orderApiService.addTransaction(
                    token = "Bearer $token",
                    addTransactionRequest = AddTransactionRequest(
                        transactionCode = transactionCode,
                        invoice = "SANTRI-${transactionCode.substring(transactionCode.length-6, transactionCode.length-1)}",
                        address = address.detailAddress,
                        district = address.district,
                        village = address.village,
                        name = name,
                        email = email,
                        phone = address.phone,
                        grossAmount = totalPrice,
                        detailRequest = detailRequests,
                    )
                )

                val transactionResponse = response.transactionResponse

                val transactionCarts = arrayListOf<Cart>()
                transactionResponse.details.forEachIndexed { index, cartResponse ->
                    val indexCart = transactionCarts.indices.find { cartResponse.name == transactionCarts[it].name }
                    if (indexCart == -1 || indexCart == null) {
                        transactionCarts.add(
                            Cart(
                                id = index,
                                name = cartResponse.name,
                                price = cartResponse.price.toDouble(),
                                imagePath = "",
                                quantity = cartResponse.quantity,
                                cartId = arrayListOf(0)
                            )
                        )
                    } else {
                        transactionCarts[index].quantity += cartResponse.quantity
                    }
                }
                emit(Resource.Success(
                    Transaction(
                        id = transactionResponse.id,
                        priceTotal = transactionResponse.grossAmount,
                        name = transactionResponse.name,
                        invoice = transactionResponse.invoice,
                        districtName = transactionResponse.district,
                        address = transactionResponse.address,
                        midtransUrl = transactionResponse.redirectUrl,
                        createdAt = transactionResponse.createdAt,
                        sendAt = transactionResponse.createdAt,
                        carts = transactionCarts,
                    )
                ))
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

    override fun getTransactions(): Flow<Resource<List<Transaction>>> = flow {
        emit(Resource.Loading())
        try {
            val token = dataStoreManager.token.first()
            if (token != "") {
                val response = orderApiService.getTransactions(
                    token = "Bearer $token",
                )

                emit(Resource.Success(response.data.data.map { transactionResponse ->

                    val statusResponse = orderApiService.getTransactionStatus(
                        "https://api.sandbox.midtrans.com/v2/${transactionResponse.invoice}/status"
                    )

                    val status = when (statusResponse.status) {
                        "capture", "settlement" -> {
                            "success"
                        }
                        "deny", "cancel", "expire" -> {
                            "failed"
                        }
                        else -> {
                            ""
                        }
                    }

                    val carts = arrayListOf<Cart>()
                    transactionResponse.details.forEachIndexed { index, cartResponse ->
                        val indexCart = carts.indices.find { cartResponse.name == carts[it].name }
                        if (indexCart == -1 || indexCart == null) {
                            carts.add(
                                Cart(
                                    id = index,
                                    name = cartResponse.name,
                                    price = cartResponse.price.toDouble(),
                                    imagePath = "",
                                    quantity = cartResponse.quantity,
                                    cartId = arrayListOf(0)
                                )
                            )
                        } else {
                            carts[index].quantity += cartResponse.quantity
                        }
                    }

                    Transaction(
                        id = transactionResponse.id,
                        priceTotal = transactionResponse.grossAmount,
                        name = transactionResponse.name,
                        invoice = transactionResponse.invoice,
                        districtName = transactionResponse.district,
                        address = transactionResponse.address,
                        midtransUrl = transactionResponse.redirectUrl,
                        createdAt = transactionResponse.createdAt,
                        sendAt = transactionResponse.createdAt,
                        carts = carts,
                        status = status
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
                    Log.d("STATUS", e.message.toString())
                    emit(Resource.Error("An unexpected error occurred"))
                }
            }
        }
    }
}