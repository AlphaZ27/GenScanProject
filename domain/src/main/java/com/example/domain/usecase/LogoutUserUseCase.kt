package com.example.domain.usecase

import com.example.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    // This use case is simple, so it doesn't need to be a suspend function.
    operator fun invoke() {
        repository.logout()
    }
}