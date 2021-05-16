package br.com.jlcampos.movie2you.data.model

import br.com.jlcampos.movie2you.utils.Constants
import com.google.gson.annotations.SerializedName

data class GenresCollection (
    @SerializedName(Constants.genres)
    var genres: List<Genres>
)