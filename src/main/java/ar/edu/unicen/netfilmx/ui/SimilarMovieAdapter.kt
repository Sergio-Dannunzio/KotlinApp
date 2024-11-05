package ar.edu.unicen.netfilmx.ui

import Movie
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ar.edu.unicen.netfilmx.app.MovieDetailActivity
import ar.edu.unicen.netfilmx.databinding.ItemSimilarBinding
import com.bumptech.glide.Glide

class SimilarMovieAdapter(private var movies: List<Movie?>) : RecyclerView.Adapter<SimilarMovieAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(private val binding: ItemSimilarBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            Glide.with(binding.root.context)
                .load(movie.getPosterUrl())
                .into(binding.movieSimilarPoster)

            binding.root.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, MovieDetailActivity::class.java).apply {
                    putExtra("movieId", movie.id)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemSimilarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        movies[position]?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int = movies.size

    fun updateMovies(newMovies: List<Movie?>) {
        movies = newMovies
        notifyDataSetChanged()
    }
}