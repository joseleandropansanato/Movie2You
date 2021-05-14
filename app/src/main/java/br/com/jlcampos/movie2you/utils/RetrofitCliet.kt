package br.com.jlcampos.movie2you.utils

import br.com.jlcampos.movie2you.data.repository.MovieRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitCliet {

    private val retrofit: Retrofit

    init {
        val httpCliet: OkHttpClient.Builder = OkHttpClient.Builder()
            .callTimeout(99999, TimeUnit.SECONDS)
            .connectTimeout(99999, TimeUnit.SECONDS)
            .readTimeout(99999, TimeUnit.SECONDS)
            .writeTimeout(99999, TimeUnit.SECONDS)
        retrofit = Retrofit.Builder()
            .baseUrl(Constants.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpCliet.build())
            .build()
    }

    val apiMove: MovieRepository
        get() = retrofit.create(MovieRepository::class.java)

    companion object {
        private var  mInstance: RetrofitCliet? = null

        @get:Synchronized
        val instance: RetrofitCliet?
            get() {
                if (mInstance == null) mInstance = RetrofitCliet()
                return mInstance
            }
    }
}