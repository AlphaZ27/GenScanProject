package com.example.core.data.models

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

/**
 * Represents a single QR code scan or generation event to be stored in Firestore.
 *
 * @param id The unique document ID from Firestore.
 * @param content The data contained within the QR code.
 * @param type The type of event (e.g., "Scanned", "Generated").
 * @param timestamp The server-side timestamp of when the event occurred.
 */

data class ScanResult(
    @DocumentId val id: String = "",
    val content: String = "",
    val type: String = "",
    @ServerTimestamp val timestamp: Date? = null
)