package com.qmantra.uservault.ui.screens


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.qmantra.uservault.data.local.User
import com.qmantra.uservault.viewmodel.UserViewModel
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment

@Composable
fun EditUserScreen(
    userId: Int,
    viewModel: UserViewModel,
    onDone: () -> Unit
) {
var user by remember { mutableStateOf<User?>(null) }
    //Load in background
    LaunchedEffect(userId) {
        user = viewModel.getUserById(userId)
    }
    //show loading instead of white screen
    if (user == null){
        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center) {
            Text("Loading...")
        }
        return
    }
    val u = user!!
    var customerId by remember { mutableStateOf(u.customerId) }
    var name by remember { mutableStateOf(u.name) }
    var phone by remember { mutableStateOf(u.phone) }
    var email by remember { mutableStateOf(u.email) }
    var bookType by remember { mutableStateOf(u.bookType) }


    Column(Modifier.padding(16.dp)
        .systemBarsPadding().padding(20.dp),) {

        OutlinedTextField(
            value = customerId,
            onValueChange = { customerId = it },
            label = { Text("Customer ID") },
            modifier = Modifier.fillMaxWidth()

        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Address") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.updateUser(
                    u.copy(
                        customerId = customerId,
                        name = name,
                        phone = phone,
                        email = email,
                        bookType = bookType
                    )
                )
                onDone()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Update")

        }
    }
}