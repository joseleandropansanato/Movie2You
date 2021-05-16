package br.com.jlcampos.movie2you.data.repository

import br.com.jlcampos.movie2you.data.model.GenresCollection
import br.com.jlcampos.movie2you.data.model.Movie
import br.com.jlcampos.movie2you.data.model.Similar
import br.com.jlcampos.movie2you.utils.Constants
import retrofit2.Response
import retrofit2.http.*

interface MovieRepository {

    @GET
    suspend fun getMovie(
        @Url url: String,
        @Header(Constants.AUTHORIZATION) token: String
    ) : Response<Movie?>

    @GET
    suspend fun getSimilarList(
        @Url url: String,
        @Header(Constants.AUTHORIZATION) token: String
    ) : Response<Similar?>

    @GET(Constants.genres_ws)
    suspend fun getGenresList(
        @Query(Constants.similar_page) page: String,
        @Header(Constants.AUTHORIZATION) token: String
    ) : Response<GenresCollection>
}