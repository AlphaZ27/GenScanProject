package com.example.feature_admin.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.core.domain.model.User


@Composable
fun AdminDashboardScreen(viewModel: AdminViewModel = viewModel()) {
    val state by viewModel.adminState.collectAsState()
    var showConfirmationDialog by remember { mutableStateOf<Pair<String, User>?>(null) }

    if (showConfirmationDialog != null) {
        val (action, user) = showConfirmationDialog!!
        ConfirmationDialog(
            action = action,
            userEmail = user.email,
            onConfirm = { when (action) {
                "Approve" -> viewModel.approveUser(user.uid)
                "Delete" -> viewModel.deleteUser(user.uid)
            }
            showConfirmationDialog = null
            },
            onDismiss = { showConfirmationDialog = null }
        )
    }

    Scaffold(
    topBar = {
        TopAppBar(title = { Text("Admin Dashboard") })
    }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when (val currentState = state) {
                is AdminState.Loading -> CircularProgressIndicator()
                is AdminState.Success -> UserList(
                    users = currentState.users,
                    onApproveClick = { user -> showConfirmationDialog = "Approve" to user },
                    onDeleteClick = { user -> showConfirmationDialog = "Delete" to user }
                )
                is AdminState.Error -> Text(
                    currentState.message,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun UserList(
    users: List<User>,
    onApproveClick: (User) -> Unit,
    onDeleteClick: (User) -> Unit
) {
    if (users.isEmpty()) {
        Text("No users found.")
    } else {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(users) { user ->
                UserItem(user, onApproveClick, onDeleteClick)
            }
        }
    }
}

@Composable
fun UserItem(
    user: User,
    onApproveClick: (User) -> Unit,
    onDeleteClick: (User) -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(user.email, style = MaterialTheme.typography.bodyLarge)
            Text("Status: ${user.status}", style = MaterialTheme.typography.bodyMedium)
            Text("Role: ${user.role}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (user.status == "pending") {
                    Button(onClick = { onApproveClick(user) }) {
                        Text("Approve")
                    }
                }
                Button(
                    onClick = { onDeleteClick(user) },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete")
                }
            }
        }
    }
}

@Composable
fun ConfirmationDialog(
    action: String,
    userEmail: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirm Action") },
        text = { Text("Are you sure you want to $action user: $userEmail? This action cannot be undone.") },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = if (action == "Delete") ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error) else ButtonDefaults.buttonColors()
            ) {
                Text(action)
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}