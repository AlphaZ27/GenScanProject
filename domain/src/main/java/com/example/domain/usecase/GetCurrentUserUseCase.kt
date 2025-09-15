package com.example.domain.usecase

import com.example.domain.repository.AuthRepository
import com.example.domain.model.User
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(): User? {
       return repository.getCurrentUser()
    }
}