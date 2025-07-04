package app.abhishekgarala.umtplay.state

data class MovieListState(
    val isLoading: Boolean = false,
    val error: String? = null
)