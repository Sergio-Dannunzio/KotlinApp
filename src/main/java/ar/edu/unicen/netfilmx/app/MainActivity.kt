package ar.edu.unicen.netfilmx.app

import MovieRepository
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.edu.unicen.netfilmx.R
import ar.edu.unicen.netfilmx.ddl.data.RetrofitInstance
import ar.edu.unicen.netfilmx.dl.MovieViewModelFactory
import ar.edu.unicen.netfilmx.ui.MovieAdapter
import ar.edu.unicen.netfilmx.ui.MovieViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var movieAdapter: MovieAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorTextView: TextView
    private lateinit var title: TextView
    private lateinit var retryButton: Button

    private val movieViewModel: MovieViewModel by viewModels {
        MovieViewModelFactory(MovieRepository(RetrofitInstance.create()))
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)
        movieAdapter = MovieAdapter(emptyList())
        errorTextView = findViewById(R.id.error)
        title = findViewById(R.id.title)
        retryButton = findViewById(R.id.retry_button)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = movieAdapter

        retryButton.setOnClickListener {
            errorTextView.visibility = View.GONE
            retryButton.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            loadMovies()
        }

        loadMovies()

    }

    private fun loadMovies() {
        progressBar.visibility = View.VISIBLE
        title.visibility = View.GONE

        movieViewModel.error.observe(this, Observer {
            errorTextView.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            title.visibility = View.GONE
            retryButton.visibility = View.VISIBLE
        })

        movieViewModel.movies.observe(this) { movies ->
            progressBar.visibility = View.GONE
            title.visibility = View.VISIBLE
            errorTextView.visibility = View.GONE
            retryButton.visibility = View.GONE
            movieAdapter.updateMovies(movies)
        }

        movieViewModel.fetchPopularMovies("5f33aacc97e47ce2ee833e5e2a2651c7")
    }

    private fun retryLoadMovies() {
        errorTextView.visibility = View.GONE
        retryButton.visibility = View.GONE
        loadMovies()
    }
}