package app.abhishekgarala.umtplay.di

import androidx.room.Room
import android.content.Context
import app.abhishekgarala.umtplay.database.AppDatabase
import app.abhishekgarala.umtplay.database.dao.MovieDao
import app.abhishekgarala.umtplay.database.dao.PendingUserDao
import app.abhishekgarala.umtplay.database.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    fun provideUserDao(database: AppDatabase): UserDao = database.userDao()

    @Provides
    fun provideMovieDao(database: AppDatabase): MovieDao = database.movieDao()

    @Provides
    fun providePendingUserDao(database: AppDatabase): PendingUserDao = database.pendingUserDao()
}