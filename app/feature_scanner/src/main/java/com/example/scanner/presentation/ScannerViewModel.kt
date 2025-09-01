package com.example.scanner.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.core.data.repository.AuthRepository
import com.example.core.data.repository.QrCodeRepository
import com.example.core.domain.model.QrCode
import com.example.core.util.Result

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

// 1. ROBUST STATE MANAGEMENT
// This allows the UI to react to loading, success (with data), and error conditions.
sealed class ScannerState {
    data class Success(val qrCodes: List<QrCode>) : ScannerState()
    data class Error(val message: String) : ScannerState()
    object Loading : ScannerState()
}


class ScannerViewModel(
    // 2. DEPENDENCIES
    // It depends on repositories from the :core module to do its work.
    private val qrCodeRepository: QrCodeRepository = QrCodeRepository(),
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _state = MutableStateFlow<ScannerState>(ScannerState.Loading)
    val state = _state.asStateFlow()

    // We need the user's ID to fetch and save their specific data.
    private val userId = authRepository.getCurrentUserUid()

    init {
        // When the ViewModel is created, immediately start fetching the user's history.
        loadHistory()
    }

    // 3. FETCHING HISTORY (New Functionality)
    private fun loadHistory() {
        if (userId != null) {
            // Listen to the real-time flow of data from the repository.
            qrCodeRepository.getQrCodesForUser(userId).onEach { result ->
                // Update the UI state based on the result from Firestore.
                _state.value = when (result) {
                    is Result.Success -> ScannerState.Success(result.data)
                    is Result.Error -> ScannerState.Error(result.message)
                    is Result.Loading -> ScannerState.Loading
                }
            }.launchIn(viewModelScope) // Launch this flow within the ViewModel's lifecycle.
        } else {
            // If the user isn't logged in, show an error.
            _state.value = ScannerState.Error("User not logged in.")
        }
    }

    // 4. HANDLING A NEW SCAN (This replaces the entire logic of the old ViewModel)
    fun onQrCodeScanned(text: String) {
        // The responsibility is no longer just updating a state variable.
        // It's now about persisting the new data to the backend.
        viewModelScope.launch {
            if (userId != null) {
                // Create a new QrCode object with the scanned text.
                val qrCode = QrCode(
                    userId = userId,
                    value = text,
                    type = "scanned" // Mark this as a scanned code.
                )
                // Use the repository to save it.
                qrCodeRepository.saveQrCode(qrCode)
                // NOTE: We don't need to manually update the state here.
                // The real-time listener in loadHistory() will automatically
                // get the new data from Firestore and update the UI for us.
            }
        }
    }
}


//class ScannerViewModel : ViewModel() {
//
//    private val repository = FirestoreRepository()
//
//    /**
//     * This function is called when the camera successfully scans a QR code.
//     * It then saves the result to Firestore.
//     * @param qrContent The string content decoded from the QR code.
//     */
//
//    fun onQrCodeScanned(qrContent: String) {
//        if (qrContent.isBlank()) return
//
//        val result = ScanResult(
//            content = qrContent,
//            type = "Scanned"
//        )
//
//        viewModelScope.launch {
//            repository.addHistoryEntry(result)
//        }
//    }
//}