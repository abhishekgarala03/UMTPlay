package app.abhishekgarala.umtplay.state

import app.abhishekgarala.umtplay.data.Movie

data class MovieDetailState(
    val movie: Movie? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)