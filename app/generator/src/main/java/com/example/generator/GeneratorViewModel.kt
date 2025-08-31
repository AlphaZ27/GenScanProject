package com.example.generator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.FirestoreRepository
import com.example.core.data.models.ScanResult
import kotlinx.coroutines.launch

class GeneratorViewModel : ViewModel() {

    private val repository = FirestoreRepository()

    /**
     * This function is called when the user wants to generate a QR code.
     * It handles the business logic for generation (which you'll add)
     * and saves the result to Firestore.
     * @param textToEncode The string content to be encoded in the QR code.
     */

    fun onGenerateQrCode(textToEncode: String) {
        if (textToEncode.isBlank()) return

        // --- BUSINESS LOGIC FOR QR CODE GENERATION GOES HERE ---
        // For example, using a library to create a Bitmap of the QR code.
        // You would expose the resulting Bitmap in a StateFlow to the UI.

        val result = ScanResult(
            content = textToEncode,
            type = "Generated"
        )

        viewModelScope.launch {
            repository.addHistoryEntry(result)
        }
    }
}