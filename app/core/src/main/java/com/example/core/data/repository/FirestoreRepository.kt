package com.example.core.data.repository

import android.util.Log
import com.example.core.data.models.ScanResult
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
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
            .addSnapshotListener { snapshot: QuerySnapshot?, error: FirebaseFirestoreException? ->
                if (error != null) {
                Log.w("FirestoreRepo", "Listen failed.", error)
                    return@addSnapshotListener
            }
                snapshot?.let { querySnapshot ->
                    _historyFlow.value = querySnapshot.toObjects(ScanResult::class.java)
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