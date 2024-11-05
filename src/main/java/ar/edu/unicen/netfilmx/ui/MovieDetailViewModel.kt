package ar.edu.unicen.netfilmx.ui

import Movie
import MovieRepository
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDetailViewModel(private val repository: MovieRepository) : ViewModel() {

    private val _movieDetails = MutableLiveData<Movie>()
    val movieDetails: LiveData<Movie> get() = _movieDetails


    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String?>()

    val similarMovies = MediatorLiveData<List<Movie>>()

    fun fetchMovieDetails(movieId: Int) {
        _isLoading.value = true
        _error.value = null

        repository.getMovieDetails(movieId).enqueue(object : Callback<Movie> {
            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _movieDetails.value = response.body()
                } else {
                    _error.value = "Error loading movie details"
                }
            }

            override fun onFailure(call: Call<Movie>, t: Throwable) {
                _isLoading.value = false
                _error.value = "Error fetching movie details: ${t.message}"
            }
        })
    }

    fun fetchSimilarMovies(movieId: Int) {
        similarMovies.addSource(repository.getSimilarMovies(movieId)) {
            similarMovies.value = it
        }
    }
}