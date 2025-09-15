package com.example.data.repository

import com.example.domain.model.User
import com.example.domain.repository.AuthRepository
import com.example.domain.util.Result
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

    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            val firebaseUser = firebaseAuth.signInWithEmailAndPassword(email, password).await().user
            firebaseUser?.let {
                Result.Success(it.toDomainUser()) // Return Result.Success
            } ?: Result.Error("User not found or login failed.") // Return Result.Error
        } catch (e: Exception) {
            Result.Error(e.message ?: "An unknown error occurred.")
        }
    }

    override suspend fun registerUser(email: String, password: String): Result<User> {
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
                Result.Success(user)
            } else {
                Result.Error("User registration failed: Firebase user object is null.")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "An unknown error occurred.")
        }
    }

    override suspend fun signup(name: String, email: String, password: String): Result<User> { // Removed kotlin. qualifier
        return registerUser(email, password)
    }

    override fun logout() {
        firebaseAuth.signOut()
    }

}

fun FirebaseUser.toDomainUser(): User {
    return User(
        uid = this.uid,
        email = this.email ?: "",
        role = "user", //Default role
        status = "pending" //Default status
    )
}