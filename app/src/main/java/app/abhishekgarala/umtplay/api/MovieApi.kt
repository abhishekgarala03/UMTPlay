package app.abhishekgarala.umtplay.api

import app.abhishekgarala.umtplay.data.MovieDto
import app.abhishekgarala.umtplay.data.MovieResponse
import retrofit2.Response
import retrofit2.http.*

interface MovieApi {
    @GET("trending/movie/day")
    suspend fun getTrendingMovies(
        @Query("page") page: Int,
        @Query("language") language: String = "en-US",
        @Header("Authorization") authorization: String = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI0YTZiNTdjNDczZWMwODIxN2U4NTU2ZGZhOWQwNWY5NCIsInN1YiI6IjY1MWJiYTIwMjIzYThiMDBhYjNkMWEwNSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.KzwyO2BmjIGyeHJCOu5XOAld_LiRAS92VTGTRQVCrA8"
    ): Response<MovieResponse>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Header("Authorization") authorization: String = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI0YTZiNTdjNDczZWMwODIxN2U4NTU2ZGZhOWQwNWY5NCIsInN1YiI6IjY1MWJiYTIwMjIzYThiMDBhYjNkMWEwNSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.KzwyO2BmjIGyeHJCOu5XOAld_LiRAS92VTGTRQVCrA8"
    ): Response<MovieDto>
}