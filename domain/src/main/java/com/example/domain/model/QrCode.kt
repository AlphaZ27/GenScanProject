package com.example.domain.model

import java.util.Date
import com.google.firebase.firestore.ServerTimestamp

/**
 * Represents a user in the application. This is a pure data class with no
 * platform-specific dependencies.
 *
 * @property userId The unique identifier who owns this QR code.
 *  @property building The building string content from the QR code.
 * @property location The location string content from the QR code.
 * @property bag The bag string content from the QR code.
 * @property type The type of QR code (e.g., "Scanned", "Generated").
 * @property timestamp The date and time when the QR code was created.
 */

data class QrCode(
    val userId: String = "", // User ID who owns the QR code
    //val building: String = "",
    //val location: String = "",
    //val bag: String = "",
    val value: String = "",
    val type: String = "", // "Scanned" or "Generated"
    @ServerTimestamp  // Automatically set the timestamp when the document is created
    val timestamp: Date? = null
)
