package com.example.domain.usecase

import com.example.domain.repository.AdminRepository
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(
    private val repository: AdminRepository
) {
    suspend operator fun invoke(uid: String) = repository.deleteUser(uid)
}