package br.com.jlcampos.movie2you.data.model

import br.com.jlcampos.movie2you.utils.Constants
import com.google.gson.annotations.SerializedName

data class Movie (
    @SerializedName(Constants.movie_id)
    var id: Int,
    @SerializedName(Constants.movie_original_title)
    var originalTitle: String?,
    @SerializedName(Constants.movie_genres)
    var genres: List<Genres>?,
    @SerializedName(Constants.movie_backdrop_path)
    var backdrop: String?,
    @SerializedName(Constants.movie_poster_path)
    var poster: String?,
    @SerializedName(Constants.movie_vote_count)
    var voteCount: Int,
    @SerializedName(Constants.movie_popularity)
    var popularity: Double,
    @SerializedName(Constants.movie_release_date)
    var releaseDate: String?,
    @SerializedName(Constants.similar_results_genre_ids)
    var genreIds: List<Int>?
)