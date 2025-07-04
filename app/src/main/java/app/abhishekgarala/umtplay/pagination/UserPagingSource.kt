package app.abhishekgarala.umtplay.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.abhishekgarala.umtplay.data.User
import app.abhishekgarala.umtplay.database.dao.UserDao
import app.abhishekgarala.umtplay.api.UserApi
import retrofit2.HttpException
import java.io.IOException

class UserPagingSource(
    private val userApi: UserApi,
    private val userDao: UserDao
) : PagingSource<Int, User>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        val page = params.key ?: 1

        return try {
            val response = userApi.getUsers(page = page)
            if (response.isSuccessful) {
                val userResponse = response.body()!!
                val users = userResponse.data.map { dto ->
                    User(
                        id = dto.id,
                        email = dto.email,
                        firstName = dto.first_name,
                        lastName = dto.last_name,
                        avatar = dto.avatar
                    )
                }

                // Cache users in database
                if (page == 1) {
                    userDao.clearUsers()
                }
                userDao.insertUsers(users)

                LoadResult.Page(
                    data = users,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (page < userResponse.total_pages) page + 1 else null
                )
            } else {
                LoadResult.Error(HttpException(response))
            }
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}