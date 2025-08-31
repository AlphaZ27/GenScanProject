package com.example.history

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.wear.compose.material.ChipDefaults

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.core.data.models.ScanResult
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryScreen(viewModel: HistoryViewModel) {
    val historyItems by viewModel.history.collectAsState()

    if (historyItems.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No history yet.")
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items = historyItems, key = { it.id }) { item ->
                HistoryItemCard(
                    item = item,
                    onDelete = { viewModel.deleteHistoryItem(item.id) }
                )
            }
        }
    }
}

@Composable
fun HistoryItemCard(item: ScanResult, onDelete: () -> Unit) {
    val dateFormatter = remember {
        SimpleDateFormat("MMM dd, yyyy - hh:mm a", Locale.getDefault())
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.content,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    val chipColor = if (item.type == "Scanned")
                        MaterialTheme.colorScheme.secondaryContainer
                    else
                        MaterialTheme.colorScheme.tertiaryContainer

                    SuggestionChip(
                        onClick = { /* No-op */ },
                        label = { Text(item.type) },
                        colors = ChipDefaults.suggestionChipColors(containerColor = chipColor)
                    )
                    Spacer(Modifier.width(8.dp))
                    item.timestamp?.let {
                        Text(
                            text = dateFormatter.format(it),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete entry",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}