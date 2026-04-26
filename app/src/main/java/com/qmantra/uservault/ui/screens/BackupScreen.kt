package com.qmantra.uservault.ui.screens

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult

import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import com.qmantra.uservault.viewmodel.UserViewModel

@Composable
fun BackupScreen(viewModel: UserViewModel){
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) {uri ->
        uri?.let {
            viewModel.restore(context,it)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Backup & Restore", style = MaterialTheme.typography.titleLarge)
        Button(onClick = {
            viewModel.backup(context)
        }) {
            Text("📥 Backup Now")
        }
        Button(onClick = {
            launcher.launch(arrayOf("application/json"))
        }) {
            Text("📤 Restore Backup")
        }
    }
}


