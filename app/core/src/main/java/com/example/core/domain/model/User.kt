package com.example.core.domain.model

data class User(
    val uid: String = "",
    val email: String = "",
    val displayName: String? = null,
    val role: String = "user", // "user" or "admin"
    val status: String = "pending" // "pending" or "approved"
)