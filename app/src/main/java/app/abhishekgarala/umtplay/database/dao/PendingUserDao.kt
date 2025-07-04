package app.abhishekgarala.umtplay.database.dao

import androidx.room.*
import app.abhishekgarala.umtplay.data.PendingUser
import kotlinx.coroutines.flow.Flow

@Dao
interface PendingUserDao {
    @Query("SELECT * FROM pending_users ORDER BY createdAt ASC")
    fun getAllPendingUsers(): Flow<List<PendingUser>>

    @Insert
    suspend fun insertPendingUser(user: PendingUser)

    @Delete
    suspend fun deletePendingUser(user: PendingUser)

    @Query("DELETE FROM pending_users")
    suspend fun clearPendingUsers()
}