package com.example.lapaksantri.presentation.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lapaksantri.domain.usecases.auth.RegisterUseCase
import com.example.lapaksantri.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {
    private val _registerResult = MutableSharedFlow<Resource<String>>()
    val registerResult = _registerResult.asSharedFlow()
    fun register(
        name: String,
        email: String,
        password: String
    ) {
        val result = registerUseCase(name, email, password)
        viewModelScope.launch {
            result.collect {
                _registerResult.emit(it)
            }
        }
    }
}