package com.example.scanner.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.GetQrCodesUseCase
import com.example.domain.usecase.SaveQrCodeUseCase
import com.example.domain.model.QrCode
import com.example.domain.usecase.GetCurrentUserUseCase
import com.example.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


// 1. ROBUST STATE MANAGEMENT
// This allows the UI to react to loading, success (with data), and error conditions.
sealed class ScannerState {
    data class Success(val qrCodes: List<QrCode>) : ScannerState()
    data class Error(val message: String) : ScannerState()
    object Loading : ScannerState()
}

@HiltViewModel
class ScannerViewModel @Inject constructor(
    // 2. DEPENDENCIES
    // It depends on repositories from the :core module to do its work.
    private val getQrCodesUseCase: GetQrCodesUseCase,
    private val saveQrCodeUseCase: SaveQrCodeUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<ScannerState>(ScannerState.Loading)
    val state = _state.asStateFlow()



    init {
        // When the ViewModel is created, immediately start fetching the user's history.
        loadHistory()
    }

    // 3. FETCHING HISTORY (New Functionality)
    private fun loadHistory() {
        val userId = getCurrentUserUseCase()?.uid
        if (userId != null) {
            // Listen to the real-time flow of data from the repository.
            getQrCodesUseCase(userId).onEach { result ->
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
    // The responsibility is no longer just updating a state variable.
    // It's now about persisting the new data to the backend.
    fun onQrCodeScanned(text: String) {
        viewModelScope.launch {
            val userId = getCurrentUserUseCase()?.uid
            if (userId != null) {
                val qrCode = QrCode(
                    uid = userId,
                    building = text,
                    location = text,
                    bag = text,
                    type = "Scanned"
                )

                // Use the repository to save it.
                saveQrCodeUseCase(qrCode)



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