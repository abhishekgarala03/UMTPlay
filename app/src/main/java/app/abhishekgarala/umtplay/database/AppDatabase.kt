package app.abhishekgarala.umtplay.database

import androidx.room.Database
import androidx.room.RoomDatabase
import app.abhishekgarala.umtplay.data.Movie
import app.abhishekgarala.umtplay.data.PendingUser
import app.abhishekgarala.umtplay.data.User
import app.abhishekgarala.umtplay.database.dao.MovieDao
import app.abhishekgarala.umtplay.database.dao.PendingUserDao
import app.abhishekgarala.umtplay.database.dao.UserDao

@Database(
    entities = [User::class, Movie::class, PendingUser::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun movieDao(): MovieDao
    abstract fun pendingUserDao(): PendingUserDao
}