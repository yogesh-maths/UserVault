package com.qmantra.uservault.data.local

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import android.content.Intent

object BackupManager {

    fun saveBackup(context: Context, json: String): Uri? {
        return try {
            val resolver = context.contentResolver

            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, "UserVault_Backup.json")
                put(MediaStore.MediaColumns.MIME_TYPE, "application/json")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }

            val uri = resolver.insert(
                MediaStore.Files.getContentUri("external"),
                contentValues
            )

            uri?.let {
                resolver.openOutputStream(it)?.use { output ->
                    output.write(json.toByteArray())
                }
            }

            uri   // 🔥 return uri

        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
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
    }
}


