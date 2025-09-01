package com.example.feature_admin.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.data.repository.AdminRepository
import com.core.domain.model.User
import com.core.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AdminState {
    data class Success(val users: List<User>) : AdminState()
    data class Error(val message: String) : AdminState()
    object Loading : AdminState()
}

class AdminViewModel(
    private val adminRepository: AdminRepository = AdminRepository() // Use DI
) : ViewModel() {

    private val _adminState = MutableStateFlow<AdminState>(AdminState.Loading)
    val adminState = _adminState.asStateFlow()

    init {
        fetchAllUsers()
    }

    private fun fetchAllUsers() {
        viewModelScope.launch {
            _adminState.value = AdminState.Loading
            adminRepository.getAllUsers().collect { result ->
                when (result) {
                    is Result.Success -> _adminState.value = AdminState.Success(result.data)
                    is Result.Error -> _adminState.value = AdminState.Error(result.message)
                    is Result.Loading -> _adminState.value = AdminState.Loading
                }
            }
        }
    }

    fun approveUser(uid: String) {
        viewModelScope.launch {
            adminRepository.updateUserStatus(uid, "approved")
            // The listener in fetchAllUsers will automatically refresh the list
        }
    }

    fun deleteUser(uid: String) {
        viewModelScope.launch {
            adminRepository.deleteUser(uid)
            // The listener in fetchAllUsers will automatically refresh the list
        }
    }
}