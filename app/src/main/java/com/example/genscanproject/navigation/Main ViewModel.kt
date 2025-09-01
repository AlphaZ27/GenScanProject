package com.example.genscanproject.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.genscanproject.core.data.repository.AuthRepository
import com.example.genscanproject.core.data.repository.UserRepository
import com.example.genscanproject.core.domain.model.User
import com.example.genscanproject.core.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Represents the user's authentication and role status
sealed class UserAuthState {
    object Loading : UserAuthState()
    object Unauthenticated : UserAuthState()
    data class Authenticated(val user: User) : UserAuthState()
    data class AuthError(val message: String) : UserAuthState()
}

class MainViewModel(
    private val authRepository: AuthRepository = AuthRepository(),
    private val userRepository: UserRepository = UserRepository()
    ) : ViewModel() {

    private val _authState = MutableStateFlow<UserAuthState>(UserAuthState.Loading)
    val authState = _authState.asStateFlow()

    init {
        checkCurrentUser()
    }

    private fun checkCurrentUser() {
        viewModelScope.launch {
            _authState.value = UserAuthState.Loading
        }
        val uid = authRepository.getCurrentUserUid()
        if (uid == null) {
            _authState.value = UserAuthState.Unauthenticated
            return@launch
        }

        when (val result = userRepository.getUser(uid)) {
            is Result.Success -> {
                _authState.value = UserAuthState.Authenticated(result.data)
            }

            is Result.Error -> {
                // Log out if user data is missing but they are authenticated
                authRepository.logout()
                _authState.value = UserAuthState.Unauthenticated
            }

            else -> {
                // Handle potential loading state from repository if you add it
            }
        }

        fun logout() {
            authRepository.logout()
            _authState.value = UserAuthState.Unauthenticated
        }
    }
}

