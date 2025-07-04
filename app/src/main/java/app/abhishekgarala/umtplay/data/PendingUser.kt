package app.abhishekgarala.umtplay.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pending_users")
data class PendingUser(
    @PrimaryKey(autoGenerate = true) val localId: Int = 0,
    val name: String,
    val job: String,
    val createdAt: Long = System.currentTimeMillis()
)