package com.example.domain.usecase

import com.example.domain.repository.AdminRepository
import javax.inject.Inject

class GetPendingUsersUseCase @Inject constructor(
    private val repository: AdminRepository
) {
    operator fun invoke() = repository.getPendingUsers()
}