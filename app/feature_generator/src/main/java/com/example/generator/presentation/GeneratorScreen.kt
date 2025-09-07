package com.example.generator.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.generator.GeneratorViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneratorScreen(
    viewModel: GeneratorViewModel = viewModel()
) {
    var text by remember { mutableStateOf("") }
    val qrCodeValue by viewModel.qrCodeValue.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("QR Code Generator") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Enter text to encode") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { viewModel.generateQrCode(text) },
                enabled = text.isNotBlank()
            ) {
                Text("Generate and Save")
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (qrCodeValue.isNotEmpty()) {
                // This is the composable from the library
                QrCode(
                    content = qrCodeValue,
                    size = 200.dp
                )
            }
        }
    }
}