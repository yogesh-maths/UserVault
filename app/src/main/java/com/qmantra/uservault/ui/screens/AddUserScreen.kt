package com.qmantra.uservault.ui.screens
import android.provider.ContactsContract
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.qmantra.uservault.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun AddUserScreen(
    viewModel: UserViewModel,
    navController: NavController
){

    var customerId by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    var expanded by remember { mutableStateOf(false) }
    var selectedBook by remember { mutableStateOf("Book 1") }

    val books = listOf("Book 1", "Book 2")
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        OutlinedTextField(
            value = customerId,
            onValueChange = { customerId = it },
            label = { Text("Customer ID") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Address") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(10.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {

            OutlinedTextField(
                value = selectedBook,
                onValueChange = {},
                readOnly = true,
                label = { Text("Book") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                books.forEach { book ->
                    DropdownMenuItem(
                        text = { Text(book) },
                        onClick = {
                            selectedBook = book
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = {

                viewModel.insertUser(
                    customerId,
                    name,
                    phone,
                    email,
                    selectedBook
                )

                Toast.makeText(context, "Saved Successfully", Toast.LENGTH_SHORT).show()

                customerId = ""
                name = ""
                phone = ""
                email = ""
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")

        }

    }

}