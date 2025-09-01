package com.example.feature_profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.data.repository.AuthRepository
import com.core.data.repository.UserRepository
import com.core.domain.model.User
import com.core.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ProfileState {
    data class Success(val user: User) : ProfileState()
    data class Error(val message: String) : ProfileState()
    object Loading : ProfileState()
}

class ProfileViewModel(
    private val authRepository: AuthRepository = AuthRepository(),
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {
    private val _userState = MutableStateFlow<ProfileState>(ProfileState.Loading)
    val userState = _userState.asStateFlow()

    init {
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            _userState.value = ProfileState.Loading
            val uid = authRepository.getCurrentUserUid()
            if (uid != null) {
                when (val result = userRepository.getUser(uid)) {
                    is Result.Success -> _userState.value = ProfileState.Success(result.data)
                    is Result.Error -> _userState.value = ProfileState.Error(result.message)
                    else -> {}
                }
            } else {
                _userState.value = ProfileState.Error("User not logged in.")
            }
        }
    }

    fun logout() {
        authRepository.logout()
    }
}