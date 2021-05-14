package br.com.jlcampos.movie2you.presentation

import android.icu.text.CompactDecimalFormat
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import br.com.jlcampos.movie2you.R
import br.com.jlcampos.movie2you.data.model.Movie
import br.com.jlcampos.movie2you.databinding.ActivityDetailMovieBinding
import br.com.jlcampos.movie2you.utils.Constants
import br.com.jlcampos.movie2you.utils.Status
import com.squareup.picasso.Picasso
import java.util.*

class DetailMovieActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: ActivityDetailMovieBinding
    private lateinit var viewModel: DetailMovieViewModel
    private lateinit var lastId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(DetailMovieViewModel::class.java)

        myUI()
        myObservers()

        getMovieAndList(viewModel.getRandomId()) // Buscando um id randomico de 100 a 150 excluindo os ids 119, 130 e 131
    }

    private fun getMovieAndList(id: String) {
        lastId = id
        viewModel.getMovie(id)
    }

    override fun onRefresh() {
        getMovieAndList(lastId)
    }

    private fun myObservers() {

        viewModel.movieLiveData.observe(this, {
            it?.let { myResource ->
                when(myResource.status) {

                    Status.SUCCESS -> {
                        hideProgressMovie()
                        changeInfo(myResource.data!!)
                    }

                    Status.ERROR -> {
                        hideProgressMovie()
                        wrong(myResource.message!!)
                    }

                    Status.LOADING -> {
                        showProgressMovie()
                    }
                }
            }
        })

    }

    private fun myUI() {
        binding.detailSwipe.setOnRefreshListener(this)
    }

    private fun hideProgressMovie() {
        binding.detailPbBackdrop.visibility = View.GONE
        if (binding.detailSwipe.isRefreshing) {
            binding.detailSwipe.isRefreshing = false
        }
    }

    private fun showProgressMovie() {
        binding.detailPbBackdrop.visibility = View.VISIBLE
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