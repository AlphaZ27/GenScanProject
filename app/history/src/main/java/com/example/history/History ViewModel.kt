package com.example.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.repository.FirestoreRepository
import com.example.core.data.models.ScanResult
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistoryViewModel : ViewModel() {

    private val repository = FirestoreRepository()

    val history: StateFlow<List<ScanResult>> = repository.historyFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun deleteHistoryItem(id: String) {
        viewModelScope.launch {
            repository.deleteHistoryEntry(id)
        }
    }
}