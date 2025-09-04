package com.example.domain.repository

import com.example.domain.model.User


interface UserRepository {
    suspend fun getUser(uid: String): Result<User>
    suspend fun saveUser(user: User): Result<Unit>
}