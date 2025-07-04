package app.abhishekgarala.umtplay.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import app.abhishekgarala.umtplay.api.UserApi
import app.abhishekgarala.umtplay.data.CreateUserRequest
import app.abhishekgarala.umtplay.data.PendingUser
import app.abhishekgarala.umtplay.data.User
import app.abhishekgarala.umtplay.database.dao.PendingUserDao
import app.abhishekgarala.umtplay.database.dao.UserDao
import app.abhishekgarala.umtplay.observer.NetworkConnectivityObserver
import app.abhishekgarala.umtplay.pagination.UserPagingSource
import app.abhishekgarala.umtplay.worker.UserSyncWorker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userApi: UserApi,
    private val userDao: UserDao,
    private val pendingUserDao: PendingUserDao,
    private val workManager: WorkManager,
    private val networkConnectivityObserver: NetworkConnectivityObserver
) {

    fun getUsers(): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(
                pageSize = 6,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                UserPagingSource(userApi, userDao)
            }
        ).flow
    }

    suspend fun createUser(name: String, job: String): Result<String> {
        return try {
            if (networkConnectivityObserver.isConnected()) {
                val response = userApi.createUser(request = CreateUserRequest(name, job))
                if (response.isSuccessful) {
                    Result.success("User created successfully")
                } else {
                    // Store offline and schedule sync
                    pendingUserDao.insertPendingUser(PendingUser(name = name, job = job))
                    scheduleUserSync()
                    Result.success("User will be synced when online")
                }
            } else {
                // Store offline and schedule sync
                pendingUserDao.insertPendingUser(PendingUser(name = name, job = job))
                scheduleUserSync()
                Result.success("User will be synced when online")
            }
        } catch (e: Exception) {
            // Store offline and schedule sync
            pendingUserDao.insertPendingUser(PendingUser(name = name, job = job))
            scheduleUserSync()
            Result.success("User will be synced when online")
        }
    }

    private fun scheduleUserSync() {
        val syncWork = OneTimeWorkRequestBuilder<UserSyncWorker>().build()
        workManager.enqueue(syncWork)
    }

    suspend fun syncPendingUsers() {
        val pendingUsers = pendingUserDao.getAllPendingUsers().first()

        for (pendingUser in pendingUsers) {
            try {
                val response = userApi.createUser(
                   request =  CreateUserRequest(pendingUser.name, pendingUser.job)
                )
                if (response.isSuccessful) {
                    pendingUserDao.deletePendingUser(pendingUser)
                }
            } catch (e: Exception) {
                // Keep in pending state for next sync attempt
                break
            }
        }
    }
}