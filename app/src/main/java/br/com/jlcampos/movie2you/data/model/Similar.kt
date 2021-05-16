package br.com.jlcampos.movie2you.data.model

import br.com.jlcampos.movie2you.utils.Constants
import com.google.gson.annotations.SerializedName

data class Similar (
    @SerializedName(Constants.similar_page)
    var page: Int,
    @SerializedName(Constants.similar_total_pages)
    var totalPages: Int,
    @SerializedName(Constants.similar_total_results)
    var totalResults: Int,
    @SerializedName(Constants.similar_results)
    var similarList: List<Movie>
)