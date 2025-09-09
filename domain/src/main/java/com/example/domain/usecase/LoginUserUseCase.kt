package com.example.domain.usecase

import com.example.domain.model.User
import com.example.domain.repository.AuthRepository
import com.example.domain.util.Result
import javax.inject.Inject

class LoginUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    // The 'invoke' operator allows you to call the class instance as a function.
    suspend operator fun invoke(email: String, password: String): Result<User> {
        return repository.login(email, password)
    }
}