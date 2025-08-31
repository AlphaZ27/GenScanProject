package com.example.scanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.genscan.core.data.FirestoreRepository
import com.example.genscan.core.data.models.ScanResult
import kotlinx.coroutines.launch

// In a real app, you would inject the repository via the constructor.
// val repository: FirestoreRepository

class ScannerViewModel : ViewModel() {

    private val repository = FirestoreRepository()

    /**
     * This function is called when the camera successfully scans a QR code.
     * It then saves the result to Firestore.
     * @param qrContent The string content decoded from the QR code.
     */

    fun onQrCodeScanned(qrContent: String) {
        if (qrContent.isBlank()) return

        val result = ScanResult(
            content = qrContent,
            type = "Scanned"
        )

        viewModelScope.launch {
            repository.addHistoryEntry(result)
        }
    }
}