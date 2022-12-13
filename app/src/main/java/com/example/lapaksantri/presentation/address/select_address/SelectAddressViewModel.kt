package com.example.lapaksantri.presentation.address.select_address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lapaksantri.domain.entities.Address
import com.example.lapaksantri.domain.usecases.address.GetAddressesUseCase
import com.example.lapaksantri.domain.usecases.address.SetMainAddressUseCase
import com.example.lapaksantri.utils.Resource
import com.example.lapaksantri.utils.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectAddressViewModel @Inject constructor(
    getAddressesUseCase: GetAddressesUseCase,
    private val setMainAddressUseCase: SetMainAddressUseCase
) : ViewModel() {
    private val _addresses = MutableStateFlow<UIState<List<Address>>>(UIState())
    val addresses =  _addresses.asStateFlow()

    private val _mainAddressId = MutableStateFlow(-1)

    private val _setMainAddressResult = MutableSharedFlow<Resource<String>>()
    val setMainAddressResult = _setMainAddressResult.asSharedFlow()

    private val _errorSnackbar = MutableSharedFlow<String>()
    val errorSnackbar = _errorSnackbar.asSharedFlow()

    init {
        getAddressesUseCase().onEach { result ->
            when(result) {
                is Resource.Success -> {
                    _addresses.value = _addresses.value.copy(
                        data = result.data,
                        isLoading = false
                    )
                    val mainAddress = _addresses.value.data?.find { it.isMain }
                    mainAddress?.let {
                        _mainAddressId.value = it.id
                    }
                }
                is Resource.Error -> {
                    _addresses.value = _addresses.value.copy(
                        data = result.data,
                        isLoading = false
                    )
                    _errorSnackbar.emit(result.message.toString())
                }
                is Resource.Loading -> {
                    _addresses.value = _addresses.value.copy(
                        data = result.data,
                        isLoading = true
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun setMainAddressId(addressId: Int) {
        _mainAddressId.value =addressId
    }

    fun setMainAddress() {
        viewModelScope.launch {
            setMainAddressUseCase(_mainAddressId.value).collect {
                _setMainAddressResult.emit(it)
            }
        }
    }
}