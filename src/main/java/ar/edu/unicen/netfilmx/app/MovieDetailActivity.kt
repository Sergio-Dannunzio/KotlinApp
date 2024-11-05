package ar.edu.unicen.netfilmx.app

import Movie
import MovieRepository
import TmdbApi
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.edu.unicen.netfilmx.R.*
import ar.edu.unicen.netfilmx.R.id.*
import ar.edu.unicen.netfilmx.ddl.data.RetrofitInstance
import ar.edu.unicen.netfilmx.dl.MovieDetailViewModelFactory
import ar.edu.unicen.netfilmx.ui.MovieDetailViewModel
import ar.edu.unicen.netfilmx.ui.SimilarMovieAdapter
import com.bumptech.glide.Glide

class MovieDetailActivity : AppCompatActivity() {

    private lateinit var apiService: TmdbApi
    private lateinit var similarMoviesAdapter: SimilarMovieAdapter
    private lateinit var movieDetailViewModel: MovieDetailViewModel
    private lateinit var movieRepository: MovieRepository
    private lateinit var progressBar: ProgressBar
    private lateinit var titleSim: TextView
    private lateinit var errorTextView: TextView
    private lateinit var retryButton: Button


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_movie_detail)
        progressBar = findViewById(id.progressBar)
        titleSim = findViewById(similaresH)
        errorTextView = findViewById(error)
        retryButton = findViewById(retry_button)


        // Configuro el cliente de Retrofit
        apiService = RetrofitInstance.create()

        // Inicializo movieRepository con apiService
        movieRepository = MovieRepository(apiService)

        // Configuro el ViewModel con el Factory
        val factory = MovieDetailViewModelFactory(movieRepository)
        movieDetailViewModel = ViewModelProvider(this, factory).get(MovieDetailViewModel::class.java)



        val movieId = intent.getIntExtra("movieId", -1)

        retryButton.setOnClickListener {
            errorTextView.visibility = View.GONE
            retryButton.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            movieDetailViewModel.fetchMovieDetails(movieId)
            movieDetailViewModel.fetchSimilarMovies(movieId)
        }

        // Configuro el adaptador y RecyclerView para películas similares
        similarMoviesAdapter = SimilarMovieAdapter(emptyList())
        findViewById<RecyclerView>(similarMoviesRecyclerView).apply {
            layoutManager = LinearLayoutManager(this@MovieDetailActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = similarMoviesAdapter
        }

        movieDetailViewModel.isLoading.observe(this) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        if (movieId != -1) {
            movieDetailViewModel.fetchMovieDetails(movieId)
            movieDetailViewModel.fetchSimilarMovies(movieId)
        }

        movieDetailViewModel.similarMovies.observe(this) { movies ->
            similarMoviesAdapter.updateMovies(movies)
        }

        movieDetailViewModel.movieDetails.observe(this) { movie ->
            movie?.let { displayMovieDetails(it) }
            titleSim.visibility = View.VISIBLE
        }
    }

    private fun displayMovieDetails(movie: Movie) {
        findViewById<TextView>(movieTitle).text = movie.title
        findViewById<TextView>(movieOverview).text = movie.overview
        findViewById<TextView>(movieVote_average).text = movie.vote_average.toString()
        val genresText = movie.genres.joinToString(", ") { it.name }
        findViewById<TextView>(movieGenre).text = genresText
        Glide.with(this)
            .load(movie.getPosterUrl())
            .into(findViewById(moviePoster))
    }

}