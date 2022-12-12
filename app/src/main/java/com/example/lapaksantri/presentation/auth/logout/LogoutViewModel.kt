package com.example.lapaksantri.presentation.auth.logout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lapaksantri.domain.usecases.auth.LogoutUseCase
import com.example.lapaksantri.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogoutViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {
    private val _logoutResult = MutableSharedFlow<Resource<Boolean>>()
    val logoutResult = _logoutResult.asSharedFlow()

    fun logout() {
        viewModelScope.launch {
            logoutUseCase().collect {
                _logoutResult.emit(it)
            }
        }
    }
}