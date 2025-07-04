package app.abhishekgarala.umtplay.database.dao

import androidx.paging.PagingSource
import androidx.room.*
import app.abhishekgarala.umtplay.data.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users ORDER BY id ASC")
    fun getAllUsers(): PagingSource<Int, User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<User>)

    @Query("DELETE FROM users")
    suspend fun clearUsers()

    @Query("SELECT COUNT(*) FROM users")
    suspend fun getUserCount(): Int
}