package com.example.genscanproject.presentation

import com.example.domain.model.User // Using your existing User model

sealed interface UserAuthState {
    object Loading : UserAuthState
    data class Authenticated(val user: User) : UserAuthState
    object Unauthenticated : UserAuthState
    data class AuthError(val message: String) : UserAuthState
}