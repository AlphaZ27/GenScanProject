package com.example.domain.usecase

import com.example.domain.repository.AuthRepository
import com.example.domain.util.Result // Your custom Result
import com.example.domain.model.User
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        // Assuming repository.registerUser returns kotlin.Result<User>
        val kotlinResult = repository.registerUser(email, password)

        // Map kotlin.Result to com.example.domain.util.Result
        return kotlinResult.fold(
            onSuccess = { user ->
                Result.Success(user) // Your custom Success type
            },
            onFailure = { throwable ->
                // Your custom Error type, using the exception's message
                Result.Error(throwable.message ?: "An unknown error occurred")
            }
        )
    }
}