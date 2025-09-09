package com.example.domain.util

/**
 * A generic sealed class that represents the result of an asynchronous operation.
 * It is used to communicate the state of data fetching from the data layer
 * up to the UI layer.
 *
 * @param T The type of the data held by the result.
 */

sealed class Result<out T> {
    /**
     * Represents a successful outcome of an operation.
     * @property data The data returned by the operation.
     */
    data class Success<out T>(val data: T) : Result<T>()

    /**
     * Represents a failed outcome of an operation.
     * @property message A user-friendly message describing the error.
     */
    data class Error(val message: String) : Result<Nothing>()

    /**
     * Represents the in-progress state of an operation.
     */
    object Loading : Result<Nothing>()
}