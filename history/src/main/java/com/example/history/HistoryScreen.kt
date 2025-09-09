package com.example.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.domain.model.QrCode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Scan History") })
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when (val currentState = state) {
                is HistoryState.Loading -> CircularProgressIndicator()
                is HistoryState.Error -> Text(
                    text = currentState.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
                is HistoryState.Success -> {
                    if (currentState.qrCodes.isEmpty()) {
                        Text("No history found.")
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(currentState.qrCodes) { qrCode ->
                                QrCodeHistoryItem(qrCode)
                            }
                        }
                    }
                }
            }
        }
    }
}

// You can reuse this composable from your ScannerScreen
@Composable
fun QrCodeHistoryItem(qrCode: QrCode) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    qrCode.value,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = qrCode.type.replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text(
                text = qrCode.timestamp?.let { java.text.SimpleDateFormat.getDateTimeInstance().format(it) } ?: "",
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}