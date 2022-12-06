package com.example.lapaksantri.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lapaksantri.domain.entities.Article
import com.example.lapaksantri.domain.entities.Slider
import com.example.lapaksantri.domain.usecases.article.GetArticlesUseCase
import com.example.lapaksantri.domain.usecases.auth.GetNameUseCase
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
) : ViewModel() {
    private val _articles = MutableStateFlow<UIState<List<Article>>>(UIState())
    val articles = _articles.asStateFlow()

    private val _sliders = MutableStateFlow<UIState<List<Slider>>>(UIState())
    val sliders = _sliders.asStateFlow()

    private val _name = MutableStateFlow<UIState<String>>(UIState())
    val name = _name.asStateFlow()

    private val _errorSnackbar = MutableSharedFlow<String>()
    val errorSnackbar = _errorSnackbar.asSharedFlow()
//
//    private val _name = MutableStateFlow<Resource<User>>(Resource.NoEvent())
//    val name = _name.asStateFlow()
//
//    private var _carts = MutableStateFlow<Resource<List<Cart>>>(Resource.NoEvent())
//    val carts: StateFlow<Resource<List<Cart>>> = _carts

    init {
        viewModelScope.launch {
//            getArticlesUseCase().collect {
//                _articles.value = it
//            }
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
//        getCartCount()
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
}