package com.example.data.repository

import com.example.data.source.FirebaseDataSource
import com.example.domain.model.QrCode
import com.example.domain.repository.QrCodeRepository
import com.example.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class QrCodeRepositoryImpl @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
): QrCodeRepository {

    override suspend fun saveQrCode(qrCode: QrCode): Result<Unit> {
        return try {
            firebaseDataSource.saveQrCode(qrCode) // Assuming this is a suspend function or handles its own threading
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to save QR code.")
        }
    }

    override fun getQrCodesForUser(userId: String): Flow<Result<List<QrCode>>> {
        return firebaseDataSource.getQrCodesForUser(userId) // Assuming this returns Flow<List<QrCode>>
            .map<List<QrCode>, Result<List<QrCode>>> { qrCodes -> Result.Success(qrCodes) }
            .onStart { emit(Result.Loading) } // Emit Loading before the actual data flow starts
            .catch { e -> emit(Result.Error(e.message ?: "An error occurred fetching QR codes.")) }
    }
}