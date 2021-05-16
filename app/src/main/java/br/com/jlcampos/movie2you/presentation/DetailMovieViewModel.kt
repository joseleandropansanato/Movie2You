package br.com.jlcampos.movie2you.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import br.com.jlcampos.movie2you.data.model.Genres
import br.com.jlcampos.movie2you.data.model.Movie
import br.com.jlcampos.movie2you.data.model.Similar
import br.com.jlcampos.movie2you.utils.AppPrefs
import br.com.jlcampos.movie2you.utils.Constants
import br.com.jlcampos.movie2you.utils.MyResult
import br.com.jlcampos.movie2you.utils.RetrofitCliet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import javax.net.ssl.HttpsURLConnection

class DetailMovieViewModel(application: Application) : AndroidViewModel(application) {

    val movieLiveData: MutableLiveData<MyResult<Movie?>> = MutableLiveData()

    val similarListLiveData: MutableLiveData<MyResult<Similar?>> = MutableLiveData()
    var newPage = 1
    private var similarResponse: MyResult<Similar?>? = null

    private var genresList: List<Genres>? = null

    fun getMovie(idMove: String) {

        similarResponse = null

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

    fun getSimilarList(idMove: String){

        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Default) {
                similarListLiveData.postValue(MyResult.load(data = null))

                try {

                    if (genresList.isNullOrEmpty()) {
                        val responseGenres = RetrofitCliet
                            .instance!!
                            .apiMove
                            .getGenresList(newPage.toString(), Constants.BEARER + Constants.TOKENv4).body()

                        genresList = responseGenres?.genres
                    }

                    val response = RetrofitCliet
                        .instance!!
                        .apiMove
                        .getSimilarList(
                            url = Constants.similar_ws_move + idMove + Constants.similar_ws,
                            token = Constants.BEARER + Constants.TOKENv4
                        )

                    if (response.code() == HttpsURLConnection.HTTP_OK) {

                        val myResult = MyResult.success(response.body())

                        handleList(myResult)

                    } else {
                        similarListLiveData.postValue(MyResult.error(data = null, message = response.message() ?: "Ooops"))
                    }

                } catch (exception: Exception) {
                    similarListLiveData.postValue(MyResult.error(data = null, message = exception.message ?: "Ooops!"))
                }
            }
        }
    }

    private fun handleList(myResult: MyResult<Similar?>) {

        newPage ++

        if (similarResponse == null) {

            if (genresList != null) {
                for (i in 0 until myResult.data?.similarList?.size!!) {
                    val mGenres = mutableListOf<Genres>()
                    for (j in 0 until myResult.data.similarList[i].genreIds?.size!!) {
                        for (k in 0 until genresList?.size!!-1) {
                            if ( genresList!![k].id == myResult.data.similarList[i].genreIds?.get(j)!!)
                                mGenres.add(genresList!![k])
                        }
                    }
                    myResult.data.similarList[i].genres = mGenres
                }
            }

            similarResponse = myResult

        } else {

            val oldList = similarResponse?.data?.similarList?.toMutableList()

            if (genresList != null) {
                for (i in 0 until myResult.data?.similarList?.size!!) {
                    val mGenres = mutableListOf<Genres>()
                    for (j in 0 until myResult.data.similarList[i].genreIds?.size!!) {
                        for (k in 0 until genresList?.size!!-1) {
                            if ( genresList!![k].id == myResult.data.similarList[i].genreIds?.get(j)!!)
                                mGenres.add(genresList!![k])
                        }
                    }
                    myResult.data.similarList[i].genres = mGenres
                }
            }

            val newList = myResult.data?.similarList?.toMutableList()

            oldList?.addAll(newList!!)
            similarResponse?.data?.similarList = oldList!!
        }

        similarListLiveData.postValue(similarResponse ?: myResult)
    }

    fun getRandomId(): String {

        var id = 0

        while ((id < 100) or (id == 119) or (id == 130) or (id == 131)) {
            id = (100..150).random()
        }

        return id.toString()
    }

    fun addFav(id: String) {
        val session = AppPrefs(getApplication())
        val myFav = JSONObject(session.getFavorites()!!)

        myFav.put(id,id)

        session.setFavorites(myFav.toString())
    }

    fun removeFavorites(id: String) {
        val session = AppPrefs(getApplication())
        val myFav = JSONObject(session.getFavorites()!!)

        myFav.remove(id)

        session.setFavorites(myFav.toString())
    }

    fun hasFav(id: String) = JSONObject(AppPrefs(getApplication()).getFavorites()!!).has(id)
}