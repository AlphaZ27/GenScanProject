package com.example.domain.usecase

import com.example.domain.repository.AuthRepository
import com.example.domain.util.Result // My custom Result
import com.example.domain.model.User
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    // The 'invoke' operator allows you to call the class instance as a function.
    // Function calls the repository to register the user and returns the result.
    suspend operator fun invoke(email: String, password: String): Result<User> {
        return repository.registerUser(email, password)
    }
}