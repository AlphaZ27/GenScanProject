package com.example.genscanproject.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.User
import com.example.domain.usecase.GetCurrentUserUseCase
import com.example.domain.usecase.LogoutUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Represents the user's authentication and role status
sealed class UserAuthState {
    object Loading : UserAuthState()
    object Unauthenticated : UserAuthState()
    data class Authenticated(val user: User) : UserAuthState()
    // removed AuthError to simplify the initial state check
}

@HiltViewModel
class MainViewModel @Inject constructor(
    // INJECT USE CASES
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val logoutUserUseCase: LogoutUserUseCase
) : ViewModel() {

    private val _authState = MutableStateFlow<UserAuthState>(UserAuthState.Loading)
    val authState = _authState.asStateFlow()

    init {
        checkCurrentUser()
    }

    private fun checkCurrentUser() {
        viewModelScope.launch {
            // The use case gets the currently authenticated user
            val user = getCurrentUserUseCase()
            if (user != null) {
                // If a user is found, we are authenticated
                _authState.value = UserAuthState.Authenticated(user)
            } else {
                // Otherwise, we are unauthenticated
                _authState.value = UserAuthState.Unauthenticated
            }
        }
    }

    // MAKE LOGOUT FUNCTION ACCESSIBLE
    fun logout() {
        viewModelScope.launch {
            logoutUserUseCase()
            _authState.value = UserAuthState.Unauthenticated
        }
    }
}

