
data class MovieResponse(
    val results: List<Movie>
)

data class Genre(
    val id: Int,
    val name: String
)

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val poster_path: String,
    val genres: List<Genre> = listOf(),
    val vote_average: Double
) {
    fun getPosterUrl(): String {
        val baseUrl = "https://image.tmdb.org/t/p/w500"
        return baseUrl + poster_path
    }
}