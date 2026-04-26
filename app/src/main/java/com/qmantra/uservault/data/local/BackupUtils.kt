package com.qmantra.uservault.data.local

import android.content.Context
import android.net.Uri

fun readBackup(context: Context, uri: Uri): String{
    return context.contentResolver
        .openInputStream(uri)
        ?.bufferedReader()
        .use{it?.readText() ?:""}
}