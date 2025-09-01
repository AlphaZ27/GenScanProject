package com.example.core.domain.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class QrCode(
    @DocumentId val id: String = "",
    val userId: String = "",
    val value: String = "",
    val type: String = "generated", // "generated" or "scanned"
    @ServerTimestamp val timestamp: Date? = null
)