package com.example.feature_profile.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onLoggedOut: () -> Unit
) {

    val state by viewModel.state.collectAsState()

    // Navigate away after a successful logout
    LaunchedEffect(state) {
        if (state is ProfileState.LoggedOut) {
            onLoggedOut()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        when (val currentState = state) {
            is ProfileState.Loading -> CircularProgressIndicator()
            is ProfileState.Error -> Text(
                text = currentState.message,
                color = MaterialTheme.colorScheme.error
            )
            is ProfileState.Success -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Text("Profile", style = MaterialTheme.typography.headlineLarge)
                    Text("Welcome!", style = MaterialTheme.typography.titleMedium)
                    Text(currentState.user.email, style = MaterialTheme.typography.bodyLarge)
                    Button(onClick = { viewModel.onLogoutClicked() }) {
                        Text("Logout")
                    }
                }
            }
            is ProfileState.LoggedOut -> {
                // This state is primarily for triggering navigation,
                // but we can show a message briefly.
                Text("Logging out...")
            }
        }
    }
}