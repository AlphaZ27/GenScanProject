package com.example.core.data


import android.util.Log
import com.example.core.data.models.ScanResult
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await

class FirestoreRepository {

    private val db = Firebase.firestore
    private val historyCollection = db.collection("scan_history")

    // A simple Flow to hold real-time updates. For a production app,
    // consider using callbackFlow for a more robust implementation.

    private val _historyFlow = MutableStateFlow<List<ScanResult>>(emptyList())
    val historyFlow: Flow<List<ScanResult>> = _historyFlow

    init {
        listenForHistoryUpdates()
    }

    private fun listenForHistoryUpdates() {
        historyCollection
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                Log.w("FirestoreRepo", "Listen failed.", error)
                    return@addSnapshotListener
            }
                snapshot?.let {
                    _historyFlow.value = it.toObjects(ScanResult::class.java)
                }
            }
    }

    /** * Adds a new scan or generation result to the Firestore collection.
    * * @param result The ScanResult object to be saved. */

    suspend fun addHistoryEntry(result: ScanResult) {
        try {
            historyCollection.add(result).await()
        } catch (e: Exception) {
            Log.e("FirestoreRepo", "Error adding document", e)
        }
    }

    /** * Deletes a specific history entry from Firestore.
     * * @param documentId The ID of the document to delete. */

    suspend fun deleteHistoryEntry(documentId: String) {
        try {
            historyCollection.document(documentId).delete().await()
        } catch (e: Exception){
            Log.e("FirestoreRepo", "Error deleting document", e)
        }
    }
}
