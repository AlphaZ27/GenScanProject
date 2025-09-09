package com.example.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.QrCode
import com.example.domain.usecase.GetCurrentUserUseCase
import com.example.domain.usecase.GetQrCodesUseCase
import com.example.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

sealed class HistoryState {
    object Loading : HistoryState()
    data class Success(val qrCodes: List<QrCode>) : HistoryState()
    data class Error(val message: String) : HistoryState()
}

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getQrCodesUseCase: GetQrCodesUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<HistoryState>(HistoryState.Loading)
    val state = _state.asStateFlow()

    init {
        loadHistory()
    }

    private fun loadHistory() {
        val userId = getCurrentUserUseCase()?.uid
        if (userId != null) {
            getQrCodesUseCase(userId).onEach { result ->
                _state.value = when (result) {
                    is Result.Success -> HistoryState.Success(result.data)
                    is Result.Error -> HistoryState.Error(result.message)
                    is Result.Loading -> HistoryState.Loading
                }
            }.launchIn(viewModelScope)
        } else {
            _state.value = HistoryState.Error("User not logged in.")
        }
    }
}