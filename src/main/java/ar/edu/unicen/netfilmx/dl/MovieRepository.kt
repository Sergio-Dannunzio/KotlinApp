import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ar.edu.unicen.netfilmx.ddl.data.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale
import javax.inject.Inject

class MovieRepository(private val tmdbApi: TmdbApi) {

    // Obtengo el idioma actual del dispositivo
    private val deviceLanguage: String = if (Locale.getDefault().language == "es") "es-ES" else "en-US"

    suspend fun getPopularMovies(apiKey: String): List<Movie> {
        return tmdbApi.getPopularMovies(apiKey, language = deviceLanguage).results
    }

    fun getMovieDetails(movieId: Int): Call<Movie> {
        return tmdbApi.getMovieDetails(movieId, "5f33aacc97e47ce2ee833e5e2a2651c7", language = deviceLanguage)
    }

    fun getSimilarMovies(movieId: Int): LiveData<List<Movie>> {
        val similarMovies = MutableLiveData<List<Movie>>()

        tmdbApi.getSimilarMovies(movieId, "5f33aacc97e47ce2ee833e5e2a2651c7").enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful) {
                    similarMovies.value = response.body()?.results ?: emptyList()
                }
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                similarMovies.value = emptyList()
            }
        })

        return similarMovies
    }
}