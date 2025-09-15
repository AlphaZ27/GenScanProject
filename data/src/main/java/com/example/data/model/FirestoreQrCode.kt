package com.example.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

// This data class is used ONLY for interacting with Firestore
data class FirestoreQrCode(
    val userId: String = "",
    val value: String = "",
    val type: String = "",
    @ServerTimestamp
    val timestamp: Date? = null
)