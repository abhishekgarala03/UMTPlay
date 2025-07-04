package app.abhishekgarala.umtplay.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.abhishekgarala.umtplay.data.Movie
import app.abhishekgarala.umtplay.database.dao.MovieDao
import app.abhishekgarala.umtplay.api.MovieApi
import retrofit2.HttpException
import java.io.IOException

class MoviePagingSource(
    private val movieApi: MovieApi,
    private val movieDao: MovieDao
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: 1

        return try {
            val response = movieApi.getTrendingMovies(page)
            if (response.isSuccessful) {
                val movieResponse = response.body()!!
                val movies = movieResponse.results.map { dto ->
                    Movie(
                        id = dto.id,
                        title = dto.title,
                        overview = dto.overview,
                        releaseDate = dto.release_date,
                        posterPath = dto.poster_path,
                        backdropPath = dto.backdrop_path,
                        voteAverage = dto.vote_average,
                        voteCount = dto.vote_count
                    )
                }

                // Cache movies in database
                if (page == 1) {
                    movieDao.clearMovies()
                }
                movieDao.insertMovies(movies)

                LoadResult.Page(
                    data = movies,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (page < movieResponse.total_pages) page + 1 else null
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

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}