package com.example.domain.usecase

import com.example.domain.repository.AuthRepository
import com.example.domain.util.Result
import com.example.domain.model.User
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        return repository.registerUser(email, password)
    }

}