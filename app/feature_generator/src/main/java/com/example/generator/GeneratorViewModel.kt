package com.example.generator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.FirestoreRepository
import com.example.domain.util.Result as ScanResult
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.QrCodeRepository
import com.example.domain.model.QrCode

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GeneratorViewModel(
    private val qrCodeRepository: QrCodeRepository = QrCodeRepository(),
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val repository = FirestoreRepository()

    // 1. STATE FOR THE UI
    // Holds the text content that the QrCode composable should display.
    // It's private so only the ViewModel can change it.

    private val _qrCodeValue = MutableStateFlow("")

    // The UI observes this public, read-only version.
    val qrCodeValue = _qrCodeValue.asStateFlow()

    // 2. PUBLIC ACTION
    // The UI calls this function when the user clicks the "Generate" button.

    fun generateQrCode(text: String) {
        // Step A: Update the UI state immediately so the user sees the new QR code.
        _qrCodeValue.value = text
        // Step B: Call the private function to handle saving the data.
        saveQrCode(text)
    }

    // 3. PRIVATE LOGIC
    // This function handles the "business logic" of saving the QR code to Firebase.

    private fun saveQrCode(text: String) {
        // Use the viewModelScope to launch a coroutine for this background task.
        viewModelScope.launch {
            // First, get the currently logged-in user's ID.
            val userId = authRepository.getCurrentUserUid()
            if (userId != null) {
                // If the user is logged in, create a QrCode data object.
                val qrCode = QrCode(
                    userId = userId,
                    value = text,
                    type = "generated" // Mark this as a generated code.
                )
                // Use the repository to save the object to Firestore.
                qrCodeRepository.saveQrCode(qrCode)
            } else {
                // In a real app, you would post an error message to the UI here.
                // For now, we can just log it or ignore it.
            }
        }
    }

    /**
     * This function is called when the user wants to generate a QR code.
     * It handles the business logic for generation (which you'll add)
     * and saves the result to Firestore.
     * @param textToEncode The string content to be encoded in the QR code.
     */

//    fun onGenerateQrCode(textToEncode: String) {
//        if (textToEncode.isBlank()) return
//
//        // --- BUSINESS LOGIC FOR QR CODE GENERATION GOES HERE ---
//        // For example, using a library to create a Bitmap of the QR code.
//        // You would expose the resulting Bitmap in a StateFlow to the UI.
//
//        val result = ScanResult(
//            content = textToEncode,
//            type = "Generated"
//        )
//
//        viewModelScope.launch {
//            repository.addHistoryEntry(result)
//        }
//    }
}