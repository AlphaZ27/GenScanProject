package com.example.feature_auth.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.feature_auth.RegistrationState
import com.example.feature_auth.RegistrationViewModel

@Composable
fun RegistrationScreen(
    viewModel: RegistrationViewModel = hiltViewModel(),
    onRegistrationSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state) {
        if (state is RegistrationState.Success) {
            onRegistrationSuccess()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Text("Create Account", style = MaterialTheme.typography.headlineMedium)
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { viewModel.registerUser(email, password) },
                enabled = state !is RegistrationState.Loading,
                modifier = Modifier.fillMaxWidth()

            ){
                Text("Sign Up")
            }

            if (state is RegistrationState.Loading) {
                CircularProgressIndicator()
            }

            if (state is RegistrationState.Error) {
                Text(
                    text = (state as RegistrationState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}