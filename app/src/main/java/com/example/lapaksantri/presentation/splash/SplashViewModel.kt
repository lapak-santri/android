package com.example.lapaksantri.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lapaksantri.domain.usecases.auth.CheckTokenUseCase
import com.example.lapaksantri.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val checkTokenUseCase: CheckTokenUseCase
) : ViewModel() {
    private val _checkTokenResult = MutableSharedFlow<Resource<Boolean>>()
    val checkTokenResult = _checkTokenResult.asSharedFlow()

    init {
        viewModelScope.launch {
            checkTokenUseCase().collect {
                _checkTokenResult.emit(it)
            }
        }
    }
}