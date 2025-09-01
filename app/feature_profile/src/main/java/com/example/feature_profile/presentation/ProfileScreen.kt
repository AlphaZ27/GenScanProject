package com.example.feature_profile.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.core.domain.model.User

@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    val userState by viewModel.userState.collectAsState()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (val state = userState) {
                is ProfileState.Loading -> CircularProgressIndicator()
                is ProfileState.Success -> UserProfile(state.user, onLogout = {
                    viewModel.logout()
                    onLogout()
                })
                is ProfileState.Error -> Text(state.message, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
fun UserProfile(user: User, onLogout: () -> Unit) {
    Text("User Profile", style = MaterialTheme.typography.headlineMedium)
    Spacer(modifier = Modifier.height(16.dp))
    Text("Email: ${user.email}")
    Text("Role: ${user.role}")
    Text("Status: ${user.status}")
    Spacer(modifier = Modifier.height(32.dp))
    Button(onClick = onLogout) {
        Text("Logout")
    }
}