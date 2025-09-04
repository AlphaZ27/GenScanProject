package com.example.domain.repository

//import com.google.firebase.auth.FirebaseUser
import com.example.domain.model.User

interface AuthRepository {
    fun getCurrentUser(): User?
    suspend fun login(email: String, password: String): Result<User>
    suspend fun signup(name: String, email: String, password: String): Result<User>
    fun logout()

    //Changed from FirebaseUser to User

}