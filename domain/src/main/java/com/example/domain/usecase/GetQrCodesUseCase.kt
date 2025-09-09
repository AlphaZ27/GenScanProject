package com.example.domain.usecase

import com.example.domain.repository.QrCodeRepository

class GetQrCodesUseCase(
    private val repository: QrCodeRepository
) {
    operator fun invoke(userId: String) = repository.getQrCodesForUser(userId)
}