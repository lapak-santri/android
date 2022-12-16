package com.example.lapaksantri.presentation.order.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lapaksantri.domain.entities.Address
import com.example.lapaksantri.domain.entities.Cart
import com.example.lapaksantri.domain.usecases.address.GetMainAddressUseCase
import com.example.lapaksantri.domain.usecases.order.DeleteCartUseCase
import com.example.lapaksantri.domain.usecases.order.GetCartsUseCase
import com.example.lapaksantri.domain.usecases.order.UpdateCartUseCase
import com.example.lapaksantri.utils.Resource
import com.example.lapaksantri.utils.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartsUseCase: GetCartsUseCase,
    getMainAddressUseCase: GetMainAddressUseCase,
    private val updateCartUseCase: UpdateCartUseCase,
    private val deleteCartUseCase: DeleteCartUseCase
) : ViewModel() {
    private val _carts = MutableStateFlow<UIState<List<Cart>>>(UIState())
    val carts =  _carts.asStateFlow()

    private val _address = MutableStateFlow<UIState<Address>>(UIState())
    val address =  _address.asStateFlow()

    private val _errorSnackbar = MutableSharedFlow<String>()
    val errorSnackbar = _errorSnackbar.asSharedFlow()

    private val _updateResult = MutableSharedFlow<Resource<String>>()
    val updateResult = _updateResult.asSharedFlow()

    private val _deleteResult = MutableSharedFlow<Resource<String>>()
    val deleteResult = _deleteResult.asSharedFlow()

    private var _totalPrice = MutableStateFlow(0.0)
    val totalPrice: StateFlow<Double> = _totalPrice

    init {
        getCart()

        getMainAddressUseCase().onEach { result ->
            when(result) {
                is Resource.Success -> {
                    _address.value = _address.value.copy(
                        data = result.data,
                        isLoading = false
                    )
                }
                is Resource.Error -> {
                    _address.value = _address.value.copy(
                        data = result.data,
                        isLoading = false
                    )
                    _errorSnackbar.emit(result.message.toString())
                }
                is Resource.Loading -> {
                    _address.value = _address.value.copy(
                        data = result.data,
                        isLoading = true
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getCart() {
        getCartsUseCase().onEach { result ->
            when(result) {
                is Resource.Success -> {
                    _carts.value = _carts.value.copy(
                        data = result.data,
                        isLoading = false
                    )
                    updateTotalPrice()
                }
                is Resource.Error -> {
                    _carts.value = _carts.value.copy(
                        data = result.data,
                        isLoading = false
                    )
                    _errorSnackbar.emit(result.message.toString())
                }
                is Resource.Loading -> {
                    _carts.value = _carts.value.copy(
                        data = result.data,
                        isLoading = true
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun updateCart(cartId: Int, type: String) {
        viewModelScope.launch {
            updateCartUseCase(cartId, type).collect {
                _updateResult.emit(it)
            }
        }
    }

    fun deleteCart(cartId: List<Int>) {
        viewModelScope.launch {
            deleteCartUseCase(cartId).collect {
                _deleteResult.emit(it)
            }
        }
    }

    private fun updateTotalPrice() {
        var totalPrice = 0.0
        _carts.value.data?.forEach {
            totalPrice += (it.quantity * it.price)
        }
        _totalPrice.value = totalPrice
    }
}