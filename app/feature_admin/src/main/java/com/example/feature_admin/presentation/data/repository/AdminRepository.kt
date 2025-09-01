package com.example.feature_admin.presentation.data.repository

import com.core.data.FirebaseModule
import com.core.domain.model.User
import com.core.util.Result
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class AdminRepository {
    private val firestore = FirebaseModule.firestore
    private val usersCollection = firestore.collection("users")

    // Use a flow to listen for real-time updates
    fun getAllUsers(): Flow<Result<List<User>>> = callbackFlow {
        val listener = usersCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                trySend(Result.Error(error.message ?: "Error listening for user updates."))
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val users = snapshot.documents.mapNotNull { it.toObject(User::class.java) }
                trySend(Result.Success(users))
            }
        }
        awaitClose { listener.remove() }
    }

    suspend fun updateUserStatus(uid: String, status: String): Result<Unit> {
        return try {
            usersCollection.document(uid).update("status", status).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to update user status.")
        }
    }

    suspend fun deleteUser(uid: String): Result<Unit> {
        // Important: Deleting the user document does NOT delete the user from Firebase Auth.
        // You would need to use a Cloud Function to do that securely.
        // This implementation only removes them from the Firestore database.
        return try {
            usersCollection.document(uid).delete().await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to delete user.")
        }
    }
}