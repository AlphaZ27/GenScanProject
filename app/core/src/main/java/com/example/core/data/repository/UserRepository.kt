package com.example.core.data.repository

import com.core.data.FirebaseModule
import com.core.domain.model.User
import com.core.util.Result
import kotlinx.coroutines.tasks.await

class UserRepository {

    private val firestore = FirebaseModule.firestore
    private val usersCollection = firestore.collection("users")

    suspend fun getUser(uid: String): Result<User> {
        return try {
            val document = usersCollection.document(uid).get().await()
            val user = document.toObject(User::class.java)
            if (user != null) {
                Result.Success(user)
            } else {
                Result.Error("User data not found.")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Could not fetch user data.")
        }
    }
}