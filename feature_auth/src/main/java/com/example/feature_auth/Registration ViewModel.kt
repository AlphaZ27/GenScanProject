package com.example.feature_auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.RegisterUserUseCase
import com.example.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class RegistrationState {
    object Idle : RegistrationState()
    object Loading : RegistrationState()
    object Success : RegistrationState()
    data class Error(val message: String) : RegistrationState()
}

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<RegistrationState>(RegistrationState.Idle)
    val state = _state.asStateFlow()

    fun registerUser(email: String, password: String) {
        viewModelScope.launch {
            _state.value = RegistrationState.Loading
            when (val result = registerUserUseCase(email, password)) {
                is Result.Success -> _state.value = RegistrationState.Success
                is Result.Error -> _state.value = RegistrationState.Error(result.message)
                is Result.Loading -> _state.value = RegistrationState.Loading
            }
        }
    }
}