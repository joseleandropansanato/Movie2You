package br.com.jlcampos.movie2you.presentation

import android.icu.text.CompactDecimalFormat
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import br.com.jlcampos.movie2you.R
import br.com.jlcampos.movie2you.data.model.Movie
import br.com.jlcampos.movie2you.databinding.ActivityDetailMovieBinding
import br.com.jlcampos.movie2you.utils.Constants
import br.com.jlcampos.movie2you.utils.Status
import com.squareup.picasso.Picasso
import java.util.*

class DetailMovieActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailMovieBinding
    private lateinit var viewModel: DetailMovieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(DetailMovieViewModel::class.java)

        myObservers()

        viewModel.getMovie("100")
    }

    private fun myObservers() {

        viewModel.movieLiveData.observe(this, {
            it?.let { myResource ->
                when(myResource.status) {

                    Status.SUCCESS -> {
//                        hideProgressMovie()
                        changeInfo(myResource.data!!)
                    }

                    Status.ERROR -> {
                        wrong(myResource.message!!)
                    }

                    Status.LOADING -> {

                    }
                }
            }
        })

    }

    private fun changeInfo(mMovie: Movie) {
        binding.detailTvTitle.text = mMovie.originalTitle
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.detailTvLikes.text = CompactDecimalFormat.getInstance(Locale.getDefault(), CompactDecimalFormat.CompactStyle.SHORT).format(mMovie.voteCount)
        } else {
            binding.detailTvLikes.text = mMovie.voteCount.toString()
        }
        binding.detailTvViews.text = mMovie.popularity.toString()

        val urlImg = Constants.URL_PHOTO + mMovie.backdrop
        Picasso.get().load(urlImg).error(R.drawable.ic_baseline_image_24).into(binding.detailIvBackdrop)
    }

    private fun wrong(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}