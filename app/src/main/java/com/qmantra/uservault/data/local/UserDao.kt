package com.qmantra.uservault.data.local
import com.qmantra.uservault.data.local.User
import androidx.room.Query
import androidx.room.Update
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Delete



@Dao
interface UserDao {

    @Insert
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("""
    SELECT * FROM users 
    WHERE name LIKE '%' || :query || '%' 
    OR phone LIKE '%' || :query || '%' 
    OR customerId LIKE '%' || :query || '%'
""")
    suspend fun searchUsers(query: String): List<User>
    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<User>

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: Int): User?

    @Query("SELECT * FROM users WHERE customerId = :customerId LIMIT 1")
    suspend fun getUserByCustomerId(customerId: String): User?

    @Insert
    suspend fun insertAllUsers(users:List<User>)

    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()
}