package com.qmantra.uservault.ui.screens
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.qmantra.uservault.ui.components.UserItem
import com.qmantra.uservault.viewmodel.UserViewModel
import kotlinx.coroutines.delay
import coil.compose.rememberAsyncImagePainter
import androidx.compose.foundation.Image
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext


@Composable
fun SearchScreen(viewModel: UserViewModel, navController: NavController) {
    var query by remember { mutableStateOf("") }
    val allUsers by viewModel.allUsers.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val context = LocalContext.current

    var imageUri by remember { mutableStateOf<Uri?>(null) }

    LaunchedEffect(Unit) {
        val (_, savedImage) = loadProfile(context)
        imageUri = savedImage
    }

    val displayList = if (query.isEmpty()) allUsers
    else
        searchResults
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(20.dp)
    ) {

        // 🔍 Search Bar ONLY
        AnimatedSearchBar(
            query = query,
            onQueryChange = { value ->
                query = value
                viewModel.searchUsers(value)
            },
            imageUri = imageUri,   // 🔥 put before
            onProfileClick = {
                navController.navigate("profile")
            }
        )
        Spacer(Modifier.height(16.dp))

        // ➕ Button
        Button(
            onClick = {
                navController.navigate("add")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add New Customer")
        }

        Spacer(Modifier.height(10.dp))

        // 📋 List (IMPORTANT: give weight)
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(displayList) { user ->
                UserItem(
                    user = user,
                    onEditClick = {
                        navController.navigate("edit/${user.id}")
                    },
                    onDeleteConfirm = {
                        viewModel.deleteUser(it)
                    }
                )
            }
        }
    }
}
    @Composable
    fun AnimatedSearchBar(
        query: String,
        onQueryChange: (String) -> Unit,
        imageUri: Uri?,
        onProfileClick: () -> Unit,   // 🔥 add this
        modifier: Modifier = Modifier
    ) {
        val hints = listOf(
            "Search by ID",
            "Search by Name",
            "Search by Phone"
        )

        var hintIndex by remember { mutableStateOf(0) }

        LaunchedEffect(Unit) {
            while (true) {
                delay(2000)
                hintIndex = (hintIndex + 1) % hints.size
            }
        }

        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,

            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Search")
            },

            // 👤 TRAILING ICON (PROFILE)
            trailingIcon = {
                if (imageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(imageUri),
                        contentDescription = "Profile",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .clickable { onProfileClick() }
                    )
                } else {
                    IconButton(onClick = onProfileClick) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = Color(0xFF5E35B1)
                        )
                    }
                }
            },

            placeholder = {
                Text(hints[hintIndex], color = Color.LightGray)

            },

            modifier = modifier
                .fillMaxWidth()
                .height(56.dp)
                .shadow(8.dp, RoundedCornerShape(30.dp)),

            shape = RoundedCornerShape(30.dp),
            singleLine = true,

            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )
    }

    @Composable
    fun AboutScreen(navController: NavController) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(20.dp)
        ) {
            Text("About Developer", style = MaterialTheme.typography.titleLarge)

            Spacer(Modifier.height(10.dp))

            Text("Developed by Yogesh Kumbhar")

            Spacer(Modifier.height(20.dp))

            Button(onClick = { navController.popBackStack() }) {
                Text("Back")
            }
        }
    }
