package com.example.lapaksantri.data.repositories

import com.example.lapaksantri.data.local.data_store.DataStoreManager
import com.example.lapaksantri.data.remote.network.OrderApiService
import com.example.lapaksantri.data.remote.request.AddCartRequest
import com.example.lapaksantri.data.remote.request.AddTransactionRequest
import com.example.lapaksantri.data.remote.request.DetailRequest
import com.example.lapaksantri.data.remote.request.UpdateCartRequest
import com.example.lapaksantri.data.remote.response.ErrorResponse
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
import retrofit2.HttpException
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
                        invoice = "SANTRI-${transactionCode.substring(0,4)}",
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

                val dateTimeFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale("in", "ID"))
                val dateFormatted = dateTimeFormatter.parse(response.transactionResponse.createdAt)
                val calender = Calendar.getInstance()
                calender.time = dateFormatted
                calender.add(Calendar.DAY_OF_MONTH, 2)
                val sendAt = dateTimeFormatter.format(calender.time)

                emit(Resource.Success(
                    Transaction(
                    id = response.transactionResponse.id,
                    priceTotal = response.transactionResponse.grossAmount,
                    name = response.transactionResponse.name,
                    invoice = response.transactionResponse.invoice,
                    districtName = response.transactionResponse.district,
                    address = response.transactionResponse.address,
                    midtransUrl = response.transactionResponse.redirectUrl,
                    createdAt = response.transactionResponse.createdAt,
                        sendAt = sendAt
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

                emit(Resource.Success(response.data.data.map {

                    val dateTimeFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale("in", "ID"))
                    val dateFormatted = dateTimeFormatter.parse(it.createdAt)
                    val calender = Calendar.getInstance()
                    calender.time = dateFormatted
                    calender.add(Calendar.DAY_OF_MONTH, 2)
                    val sendAt = dateTimeFormatter.format(calender.time)

                    Transaction(
                        id = it.id,
                        priceTotal = it.grossAmount,
                        name = it.name,
                        invoice = it.invoice,
                        districtName = it.district,
                        address = it.address,
                        midtransUrl = it.redirectUrl,
                        createdAt = it.createdAt,
                        sendAt = sendAt
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
}