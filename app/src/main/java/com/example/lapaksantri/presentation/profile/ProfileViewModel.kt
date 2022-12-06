package com.example.lapaksantri.presentation.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lapaksantri.domain.entities.User
import com.example.lapaksantri.domain.usecases.auth.GetUserUseCase
import com.example.lapaksantri.domain.usecases.auth.SignOutUseCase
import com.example.lapaksantri.utils.Resource
import com.example.lapaksantri.utils.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
) : ViewModel() {
    private val _user = MutableStateFlow<UIState<User>>(UIState())
    val user = _user.asStateFlow()

    private val _errorSnackbar = MutableSharedFlow<String>()
    val errorSnackbar = _errorSnackbar.asSharedFlow()

    init {
        getUserUseCase().onEach { result ->
            when(result) {
                is Resource.Success -> {
                    Log.d("USER",result.data.toString())
                    _user.value = _user.value.copy(
                        data = result.data,
                        isLoading = false
                    )
                }
                is Resource.Error -> {
                    _user.value = _user.value.copy(
                        data = result.data,
                        isLoading = false
                    )
                    _errorSnackbar.emit(result.message.toString())
                }
                is Resource.Loading -> {
                    _user.value = _user.value.copy(
                        data = result.data,
                        isLoading = true
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}