package com.example.scanner.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import android.Manifest
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.pedro.qrcode.QrCodeAnalyzer
import com.example.domain.model.QrCode

@OptIn(/*ExperimentalPermissionsApi::class,*/ ExperimentalMaterial3Api::class)
@Composable
fun ScannerScreen(
    viewModel: ScannerViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    // The UI now collects the entire ScannerState object.
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        cameraPermissionState.launchPermissionRequest()
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Scanner & History") }) }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            // --- CAMERA VIEW ---
            if (cameraPermissionState.status.isGranted) {
                Box(modifier = Modifier.weight(1f)) {
                    QrCodeAnalyzer(
                        modifier = Modifier.fillMaxSize(),
                        onDetect = { scannedText ->
                            // When a code is detected, call the ViewModel's function.
                            viewModel.onQrCodeScanned(scannedText)
                            Toast.makeText(context, "Saved: $scannedText", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            } else {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text("Camera permission is required.")
                }
            }

            // --- HISTORY VIEW ---
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                Text(
                    text = "History",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(8.dp))

                // This 'when' block is how the UI handles the different states.
                when(val historyState = state) {
                    is ScannerState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    is ScannerState.Success -> {
                        if (historyState.qrCodes.isEmpty()) {
                            Text("No QR codes saved yet.", style = MaterialTheme.typography.bodyMedium)
                        } else {
                            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(historyState.qrCodes) { qrCode ->
                                    QrCodeHistoryItem(qrCode)
                                }
                            }
                        }
                    }
                    is ScannerState.Error -> Text(historyState.message, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}


@Composable
fun QrCodeHistoryItem(qrCode: QrCode) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(Modifier.weight(1f))
            {
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


//@Composable
//fun ScannerScreen(viewModel: ScannerViewModel) {
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        Column(horizontalAlignment = Alignment.CenterHorizontally) {
//            Text(
//                text = "Scanner Screen",
//                style = MaterialTheme.typography.headlineMedium,
//                textAlign = TextAlign.Center
//            )
//            Spacer(modifier = Modifier.height(16.dp))
//            Text(
//                text = "The camera view would be displayed here.",
//                modifier = Modifier.padding(16.dp),
//                textAlign = TextAlign.Center
//            )
//            Spacer(modifier = Modifier.height(32.dp))
//            Button(onClick = {
//                // This is a placeholder to simulate a scan
//                viewModel.onQrCodeScanned("Simulated scan result from camera")
//            }) {
//                Text("Simulate Scan")
//            }
//        }
//    }
//}