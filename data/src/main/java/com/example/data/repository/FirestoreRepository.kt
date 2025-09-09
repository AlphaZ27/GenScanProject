package com.example.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.example.domain.model.QrCode
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class FirestoreRepository {
    // Get the Firestore database instance
    private val db = FirebaseFirestore.getInstance()
    // Get the Firebase Authentication instance
    private val auth = FirebaseAuth.getInstance()

    /**
     * Gets the unique ID (UID) of the currently logged-in user.
     * @return A String containing the user's UID, or null if no user is logged in.
     */
    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    /**
     * Saves a QR code to the Firestore database.
     * @param qrCode The QR code object to be saved.
     */
    suspend fun saveQrCode(qrCode: QrCode) {
        // Get the current user's UID
        val userId = getCurrentUserId()
        if (userId == null) {
            Log.e("FirestoreRepository", "Cannot save QR code: User not logged in")
            return
        }
        try {
            // Go to the "history" collection, create a document with the user's ID,
            // then go to a sub-collection "scans" and add the new qrCode object.
            // Using .add() creates a document with an auto-generated ID.
            db.collection("history")
                .document(userId)
                .collection("scans")
                .add(qrCode)
                .await() // .await() makes this coroutine-friendly

            Log.d("FirestoreRepository", "Successfully saved QR code for user: $userId")

        } catch (e: Exception) {
            // If anything goes wrong (e.g., no internet), log the error.
            Log.e("FirestoreRepository", "Error saving QR code to Firestore", e)
        }

    }

}