package com.example.feature_auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.domain.repository.AuthRepository
import com.example.domain.usecase.LoginUserUseCase
import com.example.domain.usecase.RegisterUserUseCase
import com.example.domain.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// A state class to represent the UI's current state
sealed class AuthState {
    object Idle : AuthState()
    object Success : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    // Inject the specific use cases this ViewModel needs
    private val loginUserUseCase: LoginUserUseCase,
    private val registerUserUseCase: RegisterUserUseCase
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState = _authState.asStateFlow()


    fun onLoginClicked(email: String, password: String) {
        // Basic validation
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Email and password cannot be empty.")
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            when (val result = loginUserUseCase(email, password)) {
                is Result.Success -> _authState.value = AuthState.Success
                is Result.Error -> _authState.value = AuthState.Error(result.message)
                else -> { /* No-op */ }
            }
        }
    }

    // Function to handle registration**
    fun onRegisterClicked(email: String, password: String) {
        if (email.isBlank() || password.length < 6) {
            _authState.value = AuthState.Error("Please enter a valid email and a password of at least 6 characters.")
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            when (val result = registerUserUseCase(email, password)) {
                is Result.Success -> _authState.value = AuthState.Success
                is Result.Error -> _authState.value = AuthState.Error(result.message)
                else -> { /* No-op */ }
            }
        }
    }

    // Function to reset the state, e.g., after navigating away.
    fun resetState() {
        _authState.value = AuthState.Idle
    }
}