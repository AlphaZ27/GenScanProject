package com.example.domain.repository

interface UserRepository {
    suspend fun getUser(userId: String): Result<User>
    suspend fun saveUser(user: User): Result<Unit>
}