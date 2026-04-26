package com.qmantra.uservault.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.qmantra.uservault.viewmodel.UserViewModel
import coil.compose.rememberAsyncImagePainter
import com.qmantra.uservault.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.LaunchedEffect
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import kotlin.contracts.contract
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.ui.res.painterResource
@Composable
fun ProfileScreen(viewModel: UserViewModel, navController: NavController) {

    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var isEditing by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            context.contentResolver.takePersistableUriPermission(
                it,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            imageUri = it
        }
    }

    LaunchedEffect(Unit) {
        val (savedName, savedImage) = loadProfile(context)
        name = savedName
        imageUri = savedImage
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(20.dp)
            .padding(top = 80.dp),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // 👤 Profile Section
        Box {
            Image(
                painter = rememberAsyncImagePainter(
                    model = imageUri ?: R.drawable.ic_profile_placeholder
                ),
                contentDescription = "Profile",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
                    .border(2.dp, Color.White, CircleShape)
                    .clickable {
                        isEditing = true
                        launcher.launch(arrayOf("image/*"))
                    }
            )

            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF5E35B1))
                    .clickable {
                        launcher.launch(arrayOf("image/*"))
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        // ✏️ Name
        if (isEditing) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            Text(
                text = name.ifEmpty { "Your Name" },
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (isEditing) {
            Spacer(Modifier.height(12.dp))

            Button(onClick = {
                saveProfile(context, name, imageUri)
                isEditing = false

                Toast.makeText(context, "Saved ✅", Toast.LENGTH_SHORT).show()
            }) {
                Text("Save")
            }
        }

        Spacer(Modifier.height(24.dp))

        // ⚙️ Settings Card
        Card(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column {
                SettingItem(
                    title = "Backup Data",
                    icon = R.drawable.ic_backup
                ) {
                    viewModel.backup(context)
                }

                SettingItem(

                    title = "Restore Data",
                    icon = R.drawable.ic_restore
                ) {
                    navController.navigate("backup")
                }

                SettingItem(
                    title = "Share Backup",
                    icon = R.drawable.ic_share
                ) {
                    viewModel.shareBackup(context)
                }

                SettingItem(
                    title = "About Developer",
                    icon = R.drawable.ic_about
                ) {
                    navController.navigate("about")
                }

            }
        }
    }
}
@Composable
fun SettingItem(
    title: String,
    icon: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // 🔥 IMAGE HERE (inside function)
        Image(
            painter = painterResource(id = icon),
            contentDescription = title,
            modifier = Modifier.size(50.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(text = title)
    }
}
fun saveProfile(context: Context, name: String, uri: Uri?) {
    val prefs = context.getSharedPreferences("profile", Context.MODE_PRIVATE)

    prefs.edit()
        .putString("name", name)
        .putString("image", uri?.toString())
        .apply()
}
fun loadProfile(context: Context): Pair<String, Uri?> {
    val prefs = context.getSharedPreferences("profile", Context.MODE_PRIVATE)

    val name = prefs.getString("name", "") ?: ""
    val image = prefs.getString("image", null)

    return Pair(name, image?.let { Uri.parse(it) })
}
fun shareBackup(context: Context, uri: Uri) {

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "application/json"
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    context.startActivity(
        Intent.createChooser(intent, "Share Backup via")
    )
    Toast.makeText(context, "Sharing backup...", Toast.LENGTH_SHORT).show()
}



