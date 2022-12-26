package com.example.lapaksantri.presentation.order.history_order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lapaksantri.domain.entities.Transaction
import com.example.lapaksantri.domain.usecases.order.GetTransactionsUseCase
import com.example.lapaksantri.utils.Resource
import com.example.lapaksantri.utils.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class HistoryOrderViewModel @Inject constructor(
    private val getTransactionsUseCase: GetTransactionsUseCase
) : ViewModel() {
    private val _transactions = MutableStateFlow<UIState<List<Transaction>>>(UIState())
    val transactions =  _transactions.asStateFlow()

    private val _errorSnackbar = MutableSharedFlow<String>()
    val errorSnackbar = _errorSnackbar.asSharedFlow()

    init {
        getTransactions()
    }

    fun getTransactions() {
        getTransactionsUseCase().onEach { result ->
            when(result) {
                is Resource.Success -> {
                    _transactions.value = _transactions.value.copy(
                        data = result.data,
                        isLoading = false
                    )
                }
                is Resource.Error -> {
                    _transactions.value = _transactions.value.copy(
                        data = result.data,
                        isLoading = false
                    )
                    _errorSnackbar.emit(result.message.toString())
                }
                is Resource.Loading -> {
                    _transactions.value = _transactions.value.copy(
                        data = result.data,
                        isLoading = true
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}