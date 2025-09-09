package com.example.feature_admin.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.User
import com.example.domain.usecase.ApprovePendingUserUseCase
import com.example.domain.usecase.DeleteUserUseCase
import com.example.domain.usecase.GetPendingUsersUseCase
import com.example.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AdminState {
    data class Success(val pendingUsers: List<User>) : AdminState()
    data class Error(val message: String) : AdminState()
    object Loading : AdminState()
}

@HiltViewModel
class AdminViewModel @Inject constructor (
    private val getPendingUsersUseCase: GetPendingUsersUseCase,
    private val approvePendingUserUseCase: ApprovePendingUserUseCase,
    private val deleteUserUseCase: DeleteUserUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<AdminState>(AdminState.Loading)
    val state = _state.asStateFlow()

    init {
        loadPendingUsers()
    }

    private fun loadPendingUsers() {
        // Explicitly typing 'result' and assuming getPendingUsersUseCase() returns Flow<Result<List<User>>>
        getPendingUsersUseCase().onEach { result: Result<List<User>> ->
            _state.value = when (result) {
                // Changed from Result.Success<*> to Result.Success
                // With result explicitly typed as Result<List<User>>, 
                // smart cast should correctly infer result.data as List<User>
                is Result.Success -> AdminState.Success(result.data)
                is Result.Error -> AdminState.Error(result.message)
                is Result.Loading -> AdminState.Loading
                // Removed redundant 'else' branch
            }
        }.launchIn(viewModelScope)
    }

    fun approveUser(uid: String) {
        viewModelScope.launch {
            approvePendingUserUseCase(uid)
        }
    }

    fun deleteUser(uid: String) {
        viewModelScope.launch {
            deleteUserUseCase(uid)
        }
    }
}