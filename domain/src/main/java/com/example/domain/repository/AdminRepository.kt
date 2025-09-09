package com.example.domain.repository

import com.example.domain.model.User
import com.example.domain.util.Result
import kotlinx.coroutines.flow.Flow


interface AdminRepository {
    fun getPendingUsers(): Flow<Result<List<User>>>
    suspend fun approveUser(uid: String): Result<Unit>
    suspend fun deleteUser(uid: String): Result<Unit>
}

