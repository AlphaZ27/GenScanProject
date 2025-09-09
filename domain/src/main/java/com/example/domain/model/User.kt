package com.example.domain.model


/**
 * Represents a user in the application. This is a pure data class with no
 * platform-specific dependencies.
 *
 * @property uid The unique identifier from Firebase Authentication.
 * @property email The user's email address.
 * @property role The role of the user (e.g., "user", "admin").
 * @property status The approval status of the user (e.g., "pending", "approved").
 */
data class User(
    val uid: String = "",
    val email: String = "",
    val role: String = "user", // "user" or "admin"
    val status: String = "pending" // "pending" or "approved"
)