package ar.edu.unicen.netfilmx.ui

import Movie
import MovieRepository
import MovieResponse
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlinx.coroutines.launch



class MovieViewModel(private val repository: MovieRepository) : ViewModel() {

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error


    fun fetchPopularMovies(apiKey: String) {
        viewModelScope.launch {
            try {
                val movieList = repository.getPopularMovies(apiKey)
                _movies.postValue(movieList)
                Log.d("MovieViewModel", "Fetched movies: ${movieList.size}")
            } catch (e: Exception) {
                _error.postValue("Error fetching movies: ${e.message}")
            }
        }
    }


}