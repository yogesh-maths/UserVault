package com.qmantra.uservault.viewmodel

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qmantra.uservault.data.local.BackupManager
import com.qmantra.uservault.data.local.User
import com.qmantra.uservault.data.local.readBackup
import com.qmantra.uservault.data.local.repository.UserRepository
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URI

import kotlinx.coroutines.withContext


class UserViewModel(private val repository: UserRepository) : ViewModel() {

    private val _searchResults = MutableStateFlow<List<User>>(emptyList())
    val searchResults: StateFlow<List<User>> = _searchResults

    private val _allUsers = MutableStateFlow<List<User>>(emptyList())
    val allUsers: StateFlow<List<User>> = _allUsers

    init {
        loadAllUsers()
    }

    fun loadAllUsers() {
        viewModelScope.launch {
            _allUsers.value = repository.getAll()
        }
    }

    fun insertUser(
        customerId: String,
        name: String,
        phone: String,
        email: String,
        bookType: String
    ) {
        viewModelScope.launch {

            val existing = repository.getByCustomerId(customerId)

            if (existing == null) {
                repository.insert(
                    User(
                        customerId = customerId,
                        name = name,
                        phone = phone,
                        email = email,
                        bookType = bookType
                    )
                )
                loadAllUsers()
            }
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            repository.update(user)
            loadAllUsers()
        }
    }

    private fun update(user: User) {}

    fun deleteUser(user: User) {
        viewModelScope.launch {
            repository.delete(user)
            loadAllUsers()
        }
    }

    fun searchUsers(query: String) {
        viewModelScope.launch {
            _searchResults.value = repository.search(query)
        }
    }

    suspend fun getUserById(id: Int): User? {
        return repository.getById(id)
    }

    fun restore(context: Context, uri: Uri) = viewModelScope.launch(Dispatchers.IO){
        val json =  readBackup(context, uri)
        repository.restoreBackup(json)

        withContext(Dispatchers.Main){
            Toast.makeText(context, "Restore completed ✅",Toast.LENGTH_LONG).show()
        }
    }
    fun backup(context: Context) = viewModelScope.launch(Dispatchers.IO) {

        val json = repository.exportBackup()

        if (json.isEmpty()) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "No data to backup ❌", Toast.LENGTH_SHORT).show()
            }
            return@launch
        }

        val uri = BackupManager.saveBackup(context, json)

        withContext(Dispatchers.Main) {
            if (uri != null) {
                Toast.makeText(context, "Backup saved in Downloads ✅", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "Backup failed ❌", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun shareBackup(context: Context) = viewModelScope.launch(Dispatchers.IO) {
        val json = repository.exportBackup()
        val uri = BackupManager.saveBackup(context, json)

        uri?.let {
            withContext(Dispatchers.Main) {
                BackupManager.shareBackup(context, it)
            }
        }
    }

}