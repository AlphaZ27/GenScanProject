package com.example.domain.repository

import com.example.domain.model.QrCode
import com.example.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface QrCodeRepository {
    suspend fun saveQrCode(qrCode: QrCode): Result<Unit>
    fun getQrCodesForUser(userId: String): Flow<Result<List<QrCode>>>
}