package br.com.jlcampos.movie2you.data.model

import br.com.jlcampos.movie2you.utils.Constants
import com.google.gson.annotations.SerializedName

data class Genres (
    @SerializedName(Constants.movie_genres_id)
    var id: Int,
    @SerializedName(Constants.movie_genres_name)
    var name: String?
)