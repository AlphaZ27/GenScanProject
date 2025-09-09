package com.example.data.repository

import com.example.domain.model.User
import com.example.domain.repository.AdminRepository
import com.example.domain.util.Result
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AdminRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
): AdminRepository {

    override fun getPendingUsers(): Flow<Result<List<User>>> = callbackFlow {
        trySend(Result.Loading)
        val listener = firestore.collection("users")
            .whereEqualTo("status", "pending")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.Error(error.message ?: "Error fetching pending users."))
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val users = snapshot.toObjects(User::class.java)
                    trySend(Result.Success(users))
                }
            }
        awaitClose { listener.remove() }
    }

    override suspend fun approveUser(uid: String): Result<Unit> {
        return try {
            firestore.collection("users").document(uid)
                .update("status", "approved").await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Could not approve user.")
        }
    }

    override suspend fun deleteUser(uid: String): Result<Unit> {
        // Note: This only deletes the Firestore document.
        // For a full solution, you would need a Cloud Function to delete the Auth user.
        return try {
            firestore.collection("users").document(uid).delete().await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Could not delete user.")
        }
    }

}