package com.example.domain.repository

import com.example.domain.model.User
import com.example.domain.util.Result

interface AuthRepository {
    fun getCurrentUser(): User?

    suspend fun login(email: String, password: String): Result<User>

    suspend fun registerUser(email: String, password: String): Result<User>

    suspend fun signup(name: String, email: String, password: String): Result<User>

    fun logout()

}