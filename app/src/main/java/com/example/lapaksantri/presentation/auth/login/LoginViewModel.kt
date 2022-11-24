package com.example.lapaksantri.presentation.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lapaksantri.domain.usecases.LoginUseCase
import com.example.lapaksantri.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    private val _loginResult = MutableSharedFlow<Resource<String>>()
    val loginResult = _loginResult.asSharedFlow()
    fun login(
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            loginUseCase(email, password).collect {
                _loginResult.emit(it)
            }
        }
    }
}