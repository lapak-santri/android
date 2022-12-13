package com.example.lapaksantri.presentation.address.add_address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lapaksantri.domain.usecases.address.AddAddressUseCase
import com.example.lapaksantri.domain.usecases.address.DeleteAddressUseCase
import com.example.lapaksantri.domain.usecases.address.UpdateAddressUseCase
import com.example.lapaksantri.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddAddressViewModel @Inject constructor(
    private val addAddressUseCase: AddAddressUseCase,
    private val updateAddressUseCase: UpdateAddressUseCase,
    private val deleteAddressUseCase: DeleteAddressUseCase
) : ViewModel() {
    private val _addAddressResult = MutableSharedFlow<Resource<String>>()
    val addAddressResult: SharedFlow<Resource<String>> = _addAddressResult

    private val _updateAddressResult = MutableSharedFlow<Resource<String>>()
    val updateAddressResult: SharedFlow<Resource<String>> = _updateAddressResult

    private val _deleteAddressResult = MutableSharedFlow<Resource<String>>()
    val deleteAddressResult: SharedFlow<Resource<String>> = _deleteAddressResult

    fun addAddress(
        recipient: String,
        phone: String,
        village: String,
        district: String,
        detailAddress: String,
        area: String,
        isMain: Boolean,
    ) {
        viewModelScope.launch {
            addAddressUseCase(
                recipient = recipient,
                phone = phone,
                village = village,
                district = district,
                detailAddress = detailAddress,
                area = area,
                isMain = isMain,
            ).collect {
                _addAddressResult.emit(it)
            }
        }
    }

    fun updateAddress(
        id: Int,
        recipient: String,
        phone: String,
        village: String,
        district: String,
        detailAddress: String,
        area: String,
        isMain: Boolean,
    ) {
        viewModelScope.launch {
            updateAddressUseCase(
                id = id,
                recipient = recipient,
                phone = phone,
                village = village,
                district = district,
                detailAddress = detailAddress,
                area = area,
                isMain = isMain,
            ).collect {
                _updateAddressResult.emit(it)
            }
        }
    }

    fun deleteAddress(id: Int) {
        viewModelScope.launch {
            deleteAddressUseCase(id).collect {
                _deleteAddressResult.emit(it)
            }
        }
    }
}