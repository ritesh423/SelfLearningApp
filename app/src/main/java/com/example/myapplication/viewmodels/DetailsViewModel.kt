import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.MovieDetailsResponse
import com.example.myapplication.data.network.tmdbService
import com.example.myapplication.repository.MovieRepository
import kotlinx.coroutines.launch

class DetailsViewModel : ViewModel() {

    private val repository = MovieRepository(apiService = tmdbService)

    private val _details = MutableLiveData<MovieDetailsResponse>()
    val details: LiveData<MovieDetailsResponse> = _details

    data class TrailerUi(
        val playableUrl: String?,   // direct MP4/HLS for ExoPlayer (if you have one)
        val youTubeKey: String?     // YouTube key fallback (open app/browser)
    )

    private val _trailer = MutableLiveData<TrailerUi>()
    val trailer: LiveData<TrailerUi> = _trailer

    fun loadDetails(movieId: Int) {
        viewModelScope.launch {
            try {
                val d = repository.getMovieDetails(movieId)
                _details.value = d

                val mv = repository.pickBestTrailer(movieId)
                val playable = repository.resolvePlayableUrl(mv)
                _trailer.value = TrailerUi(
                    playableUrl = playable,
                    youTubeKey = if (mv?.site.equals("YouTube", true)) mv?.key else null
                )
            } catch (_: Exception) {
            }
        }
    }
}
