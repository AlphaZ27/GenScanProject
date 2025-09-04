package com.example.feature_profile.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.core.domain.model.User // Assuming this User model has email, role, and status

@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    onNavigateToScanner: () -> Unit, // Added this parameter
    onNavigateToGenerator: () -> Unit, // Added this parameter
    viewModel: ProfileViewModel = viewModel()
) {
    val userState by viewModel.userState.collectAsState()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp) // Added padding for better layout
        ) {
            when (val state = userState) {
                is ProfileState.Loading -> CircularProgressIndicator()
                is ProfileState.Success -> UserProfile(
                    user = state.user,
                    onLogout = {
                        viewModel.logout() // This implies ProfileViewModel has a logout method
                        onLogout()
                    },
                    onNavigateToScanner = onNavigateToScanner,
                    onNavigateToGenerator = onNavigateToGenerator
                )
                is ProfileState.Error -> Text(state.message, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
fun UserProfile(
    user: User,
    onLogout: () -> Unit,
    onNavigateToScanner: () -> Unit,
    onNavigateToGenerator: () -> Unit
) {
    Text("User Profile", style = MaterialTheme.typography.headlineMedium)
    Spacer(modifier = Modifier.height(16.dp))
    Text("Email: ${user.email}")
    Text("Role: ${user.role}")
    Text("Status: ${user.status}")
    Spacer(modifier = Modifier.height(32.dp))
    Button(onClick = onNavigateToScanner) {
        Text("Scan QR Code")
    }
    Spacer(modifier = Modifier.height(8.dp))
    Button(onClick = onNavigateToGenerator) {
        Text("Generate QR Code")
    }
    Spacer(modifier = Modifier.height(8.dp))
    Button(onClick = onLogout) {
        Text("Logout")
    }
}