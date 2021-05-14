package br.com.jlcampos.movie2you.utils

class Constants private constructor() {
    companion object {

        const val IDENTIFICATION = "Movie2You"

        const val URL = "https://api.themoviedb.org/3"

        const val TOKENv3 = "e87204c9998738f86b5638e62e2983da"
        const val TOKENv4 = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJlODcyMDRjOTk5ODczOGY4NmI1NjM4ZTYyZTI5ODNkYSIsInN1YiI6IjYwOWQyZTRmOTYzODY0MDAyYzJlNzY3YiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.q54p8G2Wg8SH_crf7KadaxmkjVFrqiJtkHven8jFMW4"

        const val AUTHORIZATION = "Authorization"
        const val BEARER = "Bearer "

        /** /movie/{movie_id} **/
        const val movie_ws = "/movie/"
        const val movie_id = "id"
        const val movie_original_title = "original_title"
        const val movie_popularity = "popularity"
        const val movie_vote_count = "vote_count"
        const val movie_release_date = "release_date"
        const val movie_backdrop_path = "backdrop_path"
        const val movie_poster_path = "poster_path"
        const val movie_genres = "genres"
        const val movie_genres_id = "id"
        const val movie_genres_name = "name"

    }
}