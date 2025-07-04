package app.abhishekgarala.umtplay.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import app.abhishekgarala.umtplay.api.MovieApi
import app.abhishekgarala.umtplay.data.Movie
import app.abhishekgarala.umtplay.database.dao.MovieDao
import app.abhishekgarala.umtplay.pagination.MoviePagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDao: MovieDao
) {

    fun getMovies(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                MoviePagingSource(movieApi, movieDao)
            }
        ).flow
    }

    suspend fun getMovieDetails(movieId: Int): Result<Movie> {
        return try {
            // First check local database
            val localMovie = movieDao.getMovieById(movieId)
            if (localMovie != null) {
                return Result.success(localMovie)
            }

            // If not in local DB, fetch from API
            val response = movieApi.getMovieDetails(movieId)
            if (response.isSuccessful) {
                val movieDto = response.body()!!
                val movie = Movie(
                    id = movieDto.id,
                    title = movieDto.title,
                    overview = movieDto.overview,
                    releaseDate = movieDto.release_date,
                    posterPath = movieDto.poster_path,
                    backdropPath = movieDto.backdrop_path,
                    voteAverage = movieDto.vote_average,
                    voteCount = movieDto.vote_count
                )
                Result.success(movie)
            } else {
                Result.failure(Exception("Failed to fetch movie details"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}