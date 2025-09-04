package com.example.data.source

import com.example.domain.model.QrCode
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.channels.awaitClose
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseDataSource(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    private val qrCodesCollection = firestore.collection("qrCodes")

    suspend fun saveQrCode(qrCode: QrCode) {
        qrCodesCollection.add(qrCode).await()
    }

    fun getQrCodesForUser(userId: String): Flow<List<QrCode>> = callbackFlow {
        val listener = qrCodesCollection
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    val qrCodes = snapshot.toObjects<QrCode>()
                    trySend(qrCodes.sortedByDescending { it.timestamp })
                }
            }
        awaitClose { listener.remove() }
    }

    //fun getAuth(){}

    //fun getUsers(){}
}