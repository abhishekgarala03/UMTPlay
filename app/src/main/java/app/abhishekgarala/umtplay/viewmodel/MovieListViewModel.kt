package app.abhishekgarala.umtplay.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import app.abhishekgarala.umtplay.data.Movie
import app.abhishekgarala.umtplay.repository.MovieRepository
import app.abhishekgarala.umtplay.state.MovieListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MovieListState())
    val uiState: StateFlow<MovieListState> = _uiState.asStateFlow()

    val movies: Flow<PagingData<Movie>> = movieRepository.getMovies()
        .cachedIn(viewModelScope)
}