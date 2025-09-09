package com.example.generator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.QrCode
import com.example.domain.usecase.GetCurrentUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.domain.usecase.SaveQrCodeUseCase

@HiltViewModel
class GeneratorViewModel @Inject constructor(
    private val saveQrCodeUseCase: SaveQrCodeUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {
    // CREATE a single instance of your new repository

    private val _qrCodeValue = MutableStateFlow("")
    val qrCodeValue = _qrCodeValue.asStateFlow()

    fun generateQrCode(text: String) {
        if (text.isBlank()) return
        _qrCodeValue.value = text
        saveQrCode(text)
    }

    private fun saveQrCode(text: String) {
        viewModelScope.launch {
            // Get the user ID using the GetCurrentUserUseCase
            val userId = getCurrentUserUseCase()?.uid
            if (userId != null) {
                // Create a new QrCode object using the consistent data model
                val qrCode = QrCode(
                    userId = userId, // Use 'userId' not 'uid'
                    value = text,
                    type = "generated"
                )
                // Use the SaveQrCodeUseCase to save the data.
                saveQrCodeUseCase(qrCode)
            }
        }
    }
}