package com.example.feature_auth.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.feature_auth.AuthState
import com.example.feature_auth.AuthViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegistration: () -> Unit, // Added this parameter
    viewModel: AuthViewModel = viewModel()
) {
    val state by viewModel.authState.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Navigate away on successful login
    LaunchedEffect(state) {
        if (state is AuthState.Authenticated) {
            onLoginSuccess()
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Login", style = MaterialTheme.typography.headlineMedium)
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
            OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") })

            if (state is AuthState.Loading) {
                CircularProgressIndicator()
            }

            if (state is AuthState.Error) {
                Text(text = (state as AuthState.Error).message, color = MaterialTheme.colorScheme.error)
            }

            Button(onClick = { viewModel.login(email, password) }, enabled = state !is AuthState.Loading) {
                Text("Login")
            }

            // Add a sign-up button here
            Button(onClick = onNavigateToRegistration) { // Used the new parameter here
                Text("Sign Up")
            }

        }
    }
}