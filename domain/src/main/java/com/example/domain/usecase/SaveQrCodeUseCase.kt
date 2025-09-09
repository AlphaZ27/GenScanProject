package com.example.domain.usecase

import com.example.domain.model.QrCode
import com.example.domain.repository.QrCodeRepository

class SaveQrCodeUseCase(
    private val repository: QrCodeRepository
) {
    suspend operator fun invoke(qrCode: QrCode) = repository.saveQrCode(qrCode)
}