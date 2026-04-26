package com.qmantra.uservault.ui.components

import androidx.compose.runtime.Composable
import com.qmantra.uservault.data.local.User
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Info

@Composable
fun UserItem(
    user: User,
    onEditClick: () -> Unit,
    onDeleteConfirm: (User) -> Unit
) {

    var showDialog by remember { mutableStateOf(false) }

    // 🔴 Dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    onDeleteConfirm(user)
                    showDialog = false
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("No")
                }
            },
            title = { Text("Delete User") },
            text = { Text("Are you sure you want to delete this user?") }
        )
    }

    // 🧾 Card UI
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Column(Modifier.padding(12.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Info, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "ID: ${user.customerId}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Person, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(user.name, style = MaterialTheme.typography.titleMedium)
            }

            Spacer(Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Phone, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(user.phone)
            }

            Spacer(Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Book, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(user.bookType)
            }

            Spacer(Modifier.height(10.dp))

            // ✅ Buttons aligned properly
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {

                OutlinedButton(onClick = onEditClick) {
                    Text("Edit")
                }

                Spacer(Modifier.width(8.dp))

                Button(
                    onClick = { showDialog = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Gray.copy(alpha = 0.4f)
                    )
                ) {
                    Text("Delete")
                }
            }
        }
    }
}


