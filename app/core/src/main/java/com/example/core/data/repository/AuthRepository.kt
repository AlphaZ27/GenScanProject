package com.example.core.data.repository

import com.data.FirebaseModule
import com.core.domain.model.User
import com.core.util.Result
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.tasks.await


class AuthRepository {

    private val auth = FirebaseModule.auth
    private val firestore = FirebaseModule.firestore

    suspend fun login(email: String, password: String): Result<AuthResult> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Result.Success(result)
        } catch (e: Exception) {
            Result.Error(e.message ?: "An unknown error occurred.")
        }
    }

    suspend fun signup(email: String, password: String, displayName: String): Result<Unit> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user
            if (firebaseUser != null) {
                val user = User(
                    uid = firebaseUser.uid,
                    email = email,
                    displayName = displayName,
                    role = "user",
                    status = "pending"
                )
                firestore.collection("users").document(firebaseUser.uid).set(user).await()
                Result.Success(Unit)
            } else {
                Result.Error("User could not be created.")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "An unknown error occurred during sign up.")
        }
    }

    fun logout() {
        auth.signOut()
    }

    fun getCurrentUserUid(): String? {
        return auth.currentUser?.uid
    }
}