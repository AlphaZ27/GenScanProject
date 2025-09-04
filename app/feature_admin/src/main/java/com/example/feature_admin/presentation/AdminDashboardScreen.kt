package com.example.feature_admin.presentation

// Standard Compose imports
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
// LazyColumn and items are already imported but might be unresolved due to Gradle
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
// Material 3 components
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
// Runtime
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
// UI
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
// Hilt ViewModel (already imported but might be unresolved due to Gradle)
import androidx.hilt.navigation.compose.hiltViewModel

import com.example.domain.model.User
// AdminViewModel and AdminState are in the same package.

@Composable
fun AdminDashboardScreen(viewModel: AdminViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()

    when (val currentState = state) {
        is AdminState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is AdminState.Success -> {
            PendingUsersList(
                users = currentState.pendingUsers,
                onApprove = { user -> viewModel.approveUser(user.uid) }, // Corrected
                onDelete = { user -> viewModel.deleteUser(user.uid) }  // Corrected
            )
        }
        is AdminState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(currentState.message)
            }
        }
    }
}

@Composable
fun PendingUsersList(
    users: List<User>,
    onApprove: (User) -> Unit,
    onDelete: (User) -> Unit
) {
    if (users.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No pending users found.")
        }
        return
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(users) { user -> // 'user' here is a User object
            UserApprovalCard(user = user, onApprove = onApprove, onDelete = onDelete)
        }
    }
}

@Composable
fun UserApprovalCard(
    user: User,
    onApprove: (User) -> Unit,
    onDelete: (User) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = user.email ?: "No email", modifier = Modifier.weight(1f)) // Added null check
            Row(
                modifier = Modifier.padding(8.dp)
            ) {
                Button(onClick = { onApprove(user) }) { // Corrected: pass the User object
                    Text("Approve")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(onClick = { onDelete(user) }) { // Corrected: pass the User object
                    Text("Delete")
                }
            }
        }
    }
}