package br.com.jlcampos.movie2you.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import br.com.jlcampos.movie2you.data.model.Movie
import br.com.jlcampos.movie2you.utils.Constants
import br.com.jlcampos.movie2you.utils.MyResult
import br.com.jlcampos.movie2you.utils.RetrofitCliet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.net.ssl.HttpsURLConnection

class DetailMovieViewModel(application: Application) : AndroidViewModel(application) {

    val movieLiveData: MutableLiveData<MyResult<Movie?>> = MutableLiveData()

    fun getMovie(idMove: String) {

        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Default) {
                movieLiveData.postValue(MyResult.load(data = null))

                try {

                    val response = RetrofitCliet
                        .instance!!
                        .apiMove
                        .getMovie(Constants.movie_ws + idMove, Constants.BEARER + Constants.TOKENv4)

                    if (response.code() == HttpsURLConnection.HTTP_OK) {

                        val myResult = MyResult.success(response.body())

                        movieLiveData.postValue(myResult)

                    } else {
                        movieLiveData.postValue(MyResult.error(data = null, message = response.message() ?: "Ooops!"))
                    }

                } catch (exception: Exception) {
                    movieLiveData.postValue(MyResult.error(data = null, message = exception.message ?: "Ooops!"))
                }
            }
        }

    }

}