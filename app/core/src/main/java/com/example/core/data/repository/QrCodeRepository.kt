package com.example.core.data.repository

import com.example.core.data.FirebaseModule
import com.example.core.domain.model.QrCode
import com.example.core.util.Result
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class QrCodeRepository {

    private val firestore = FirebaseModule.firestore
    private val qrCodesCollection = firestore.collection("qrCodes")

    suspend fun saveQrCode(qrCode: QrCode): Result<Unit> {
        return try {
            qrCodesCollection.add(qrCode).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to save QR code.")
        }
    }

    fun getQrCodesForUser(userId: String): Flow<Result<List<QrCode>>> = callbackFlow {
        val listener = qrCodesCollection
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.Error(error.message ?: "Error listening for QR code updates."))
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val qrCodes = snapshot.toObjects(QrCode::class.java)
                    trySend(Result.Success(qrCodes.sortedByDescending { it.timestamp }))
                }
            }
        awaitClose { listener.remove() }
    }
}