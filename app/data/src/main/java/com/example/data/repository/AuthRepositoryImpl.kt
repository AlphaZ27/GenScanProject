package com.example.data.repository

import com.example.domain.model.User
import com.example.domain.repository.AuthRepository
// com.example.domain.util.Result import is no longer needed as we're using kotlin.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override fun getCurrentUser(): User? {
        return firebaseAuth.currentUser?.toDomainUser()
    }

    override suspend fun login(email: String, password: String): Result<User> { // Removed kotlin. qualifier
        return try {
            val firebaseUser = firebaseAuth.signInWithEmailAndPassword(email, password).await().user
            firebaseUser?.let {
                Result.success(it.toDomainUser()) // Removed kotlin. qualifier
            } ?: Result.failure(Exception("User not found or login failed.")) // Removed kotlin. qualifier
        } catch (e: Exception) {
            Result.failure(e) // Removed kotlin. qualifier
        }
    }

    override suspend fun registerUser(email: String, password: String): Result<User> { // Removed kotlin. qualifier
        return try {
            val userCredential = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = userCredential.user
            if (firebaseUser != null) {
                val user = User(
                    uid = firebaseUser.uid,
                    email = firebaseUser.email ?: "",
                    role = "user",
                    status = "pending"
                )
                firestore.collection("users").document(firebaseUser.uid).set(user).await()
                Result.success(user) // Removed kotlin. qualifier
            } else {
                Result.failure(Exception("User registration failed: Firebase user object is null.")) // Removed kotlin. qualifier
            }
        } catch (e: Exception) {
            Result.failure(e) // Removed kotlin. qualifier
        }
    }

    override suspend fun signup(name: String, email: String, password: String): Result<User> { // Removed kotlin. qualifier
        return try {
            val firebaseUser = firebaseAuth.createUserWithEmailAndPassword(email, password).await().user
            firebaseUser?.let {
                Result.success(it.toDomainUser()) // Removed kotlin. qualifier
            } ?: Result.failure(Exception("Signup failed: Could not create user.")) // Removed kotlin. qualifier
        } catch (e: Exception) {
            Result.failure(e) // Removed kotlin. qualifier
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }

    // Add GetPendingUsers function here
    // Add ApprovePendingUser function here
    // Add DeleteUser function here
}

fun FirebaseUser.toDomainUser(): User {
    return User(
        uid = this.uid,
        email = this.email ?: "",
        role = "user",
        status = "pending"
    )
}