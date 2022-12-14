package com.example.lapaksantri.presentation.order.add_order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lapaksantri.domain.entities.Cart
import com.example.lapaksantri.domain.entities.Product
import com.example.lapaksantri.domain.usecases.order.AddCartsUseCase
import com.example.lapaksantri.domain.usecases.order.GetProductsUseCase
import com.example.lapaksantri.utils.Resource
import com.example.lapaksantri.utils.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddOrderViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val addCartsUseCase: AddCartsUseCase,
) : ViewModel() {
    private val _products = MutableStateFlow<UIState<List<Product>>>(UIState())
    val products =  _products.asStateFlow()

    private val _errorSnackbar = MutableSharedFlow<String>()
    val errorSnackbar = _errorSnackbar.asSharedFlow()

    private val _addCartsResult = MutableSharedFlow<Resource<String>>()
    val addCartsResult = _addCartsResult.asSharedFlow()

    private val _carts = MutableStateFlow<ArrayList<Cart>>(arrayListOf())

    init {
        getProducts()
    }

    fun getProducts() {
        getProductsUseCase().onEach { result ->
            when(result) {
                is Resource.Success -> {
                    _products.value = _products.value.copy(
                        data = result.data,
                        isLoading = false
                    )
                }
                is Resource.Error -> {
                    _products.value = _products.value.copy(
                        data = result.data,
                        isLoading = false
                    )
                    _errorSnackbar.emit(result.message.toString())
                }
                is Resource.Loading -> {
                    _products.value = _products.value.copy(
                        data = result.data,
                        isLoading = true
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun plusCart(product: Product) {
        val index = _carts.value.indices.find { product.id == _carts.value[it].id }
        index?.let {
            _carts.value[it].quantity++
        } ?: kotlin.run {
            _carts.value.add(Cart(
                id = product.id,
                name = product.name,
                price = product.price,
                imagePath = product.imagePath,
                quantity = 1,
                cartId = arrayListOf()
            ))
        }
    }

    fun minCart(product: Product) {
        val index = _carts.value.indices.find { product.id == _carts.value[it].id }
        index?.let {
            if (_carts.value[it].quantity > 1) {
                _carts.value[it].quantity--
            } else {
                _carts.value.removeAt(it)
            }
        }
    }

    fun addCarts() {
        viewModelScope.launch {
            if (_carts.value.isEmpty()) {
                _errorSnackbar.emit("Pilih Produk Terlebih Dahulu")
            } else {
                addCartsUseCase.invoke(_carts.value).collect {
                    _addCartsResult.emit(it)
                }
            }
        }
    }

    fun clearCarts() {
        _carts.value = arrayListOf()
    }
}