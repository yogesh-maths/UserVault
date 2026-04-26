package com.qmantra.uservault.data.local.repository
import com.qmantra.uservault.data.local.BackupData
import com.qmantra.uservault.data.local.User
import com.qmantra.uservault.data.local.UserDao
import com.google.gson.Gson

class UserRepository(private val dao: UserDao) {

    suspend fun insert(user: User) = dao.insertUser(user)

    suspend fun update(user: User) = dao.updateUser(user)

    suspend fun delete(user: User) = dao.deleteUser(user)

    suspend fun search(query: String) =
        dao.searchUsers(query)

    suspend fun getAll() = dao.getAllUsers()

    suspend fun getById(id: Int) = dao.getUserById(id)

    suspend fun getByCustomerId(id: String) =
        dao.getUserByCustomerId(id)
    //New : Export backup
    suspend fun exportBackup(): String{
        val users =dao.getAllUsers()
        val backup = BackupData(users)
        return Gson().toJson(backup)
    }
    //New: restore backup
    suspend fun restoreBackup(json: String){
        val backup = Gson().fromJson(json, BackupData::class.java)

        dao.deleteAllUsers()
        dao.insertAllUsers(backup.users)
    }
}


