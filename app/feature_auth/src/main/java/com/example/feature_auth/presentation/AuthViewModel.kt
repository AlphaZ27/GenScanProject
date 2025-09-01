package com.example.feature_auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.data.repository.AuthRepository
import com.core.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Unauthenticated : AuthState()
    object Authenticated : AuthState()
    data class Error(val message: String) : AuthState()
    object Loading : AuthState()
}


class AuthViewModel(
    private val authRepository: AuthRepository = AuthRepository() // Use DI in a real app
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState = _authState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            when (val result = authRepository.login(email, password)) {
                is Result.Success -> _authState.value = AuthState.Authenticated
                is Result.Error -> _authState.value = AuthState.Error(result.message)
                else -> {} // Should not happen
            }
        }
    }
}