package com.example.lapaksantri.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lapaksantri.domain.entities.Article
import com.example.lapaksantri.domain.entities.Slider
import com.example.lapaksantri.domain.usecases.article.GetArticlesUseCase
import com.example.lapaksantri.domain.usecases.auth.GetNameUseCase
import com.example.lapaksantri.domain.usecases.order.GetCartsUseCase
import com.example.lapaksantri.domain.usecases.slider.GetSlidersUseCase
import com.example.lapaksantri.utils.Resource
import com.example.lapaksantri.utils.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    getSlidersUseCase: GetSlidersUseCase,
    getArticlesUseCase: GetArticlesUseCase,
    getNameUseCase: GetNameUseCase,
    private val getCartsUseCase: GetCartsUseCase,
) : ViewModel() {
    private val _articles = MutableStateFlow<UIState<List<Article>>>(UIState())
    val articles = _articles.asStateFlow()

    private val _sliders = MutableStateFlow<UIState<List<Slider>>>(UIState())
    val sliders = _sliders.asStateFlow()

    private val _name = MutableStateFlow<UIState<String>>(UIState())
    val name = _name.asStateFlow()

    private val _errorSnackbar = MutableSharedFlow<String>()
    val errorSnackbar = _errorSnackbar.asSharedFlow()

    private var _cartCount = MutableStateFlow<UIState<Int>>(UIState())
    val cartCount: StateFlow<UIState<Int>> = _cartCount

    init {
        getCartCount()
        viewModelScope.launch {
            getSlidersUseCase().onEach { result ->
                when(result) {
                    is Resource.Success -> {
                        _sliders.value = _sliders.value.copy(
                            data = result.data,
                            isLoading = false
                        )
                    }
                    is Resource.Error -> {
                        _sliders.value = _sliders.value.copy(
                            data = result.data,
                            isLoading = false
                        )
                        _errorSnackbar.emit(result.message.toString())
                    }
                    is Resource.Loading -> {
                        _sliders.value = _sliders.value.copy(
                            data = result.data,
                            isLoading = true
                        )
                    }
                }
            }.launchIn(viewModelScope)
            getArticlesUseCase().onEach { result ->
                when(result) {
                    is Resource.Success -> {
                        _articles.value = _articles.value.copy(
                            data = result.data,
                            isLoading = false
                        )
                    }
                    is Resource.Error -> {
                        _articles.value = _articles.value.copy(
                            data = result.data,
                            isLoading = false
                        )
                        _errorSnackbar.emit(result.message.toString())
                    }
                    is Resource.Loading -> {
                        _articles.value = _articles.value.copy(
                            data = result.data,
                            isLoading = true
                        )
                    }
                }
            }.launchIn(viewModelScope)
            getNameUseCase().onEach { result ->
                when(result) {
                    is Resource.Success -> {
                        _name.value = _name.value.copy(
                            data = result.data,
                            isLoading = false
                        )
                    }
                    is Resource.Error -> {
                        _name.value = _name.value.copy(
                            data = result.data,
                            isLoading = false
                        )
                        _errorSnackbar.emit(result.message.toString())
                    }
                    is Resource.Loading -> {
                        _name.value = _name.value.copy(
                            data = result.data,
                            isLoading = true
                        )
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun getTime(): String {
        val calendar = Calendar.getInstance()
        return when (calendar.get(Calendar.HOUR_OF_DAY)) {
            in 0..11 -> {
                "Selamat Pagi"
            }
            in 12..14 -> {
                "Selamat Siang"
            }
            in 15..17 -> {
                "Selamat Sore"
            }
            else -> {
                "Selamat Malam"
            }
        }
    }

    fun getCartCount() {
        getCartsUseCase().onEach { result ->
            when(result) {
                is Resource.Success -> {
                    _cartCount.value = _cartCount.value.copy(
                        data = result.data?.size,
                        isLoading = false
                    )
                }
                is Resource.Error -> {
                    _cartCount.value = _cartCount.value.copy(
                        data = 0,
                        isLoading = false
                    )
                    _errorSnackbar.emit(result.message.toString())
                }
                is Resource.Loading -> {
                    _cartCount.value = _cartCount.value.copy(
                        data = 0,
                        isLoading = true
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}