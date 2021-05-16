package br.com.jlcampos.movie2you.presentation

import android.icu.text.CompactDecimalFormat
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.jlcampos.movie2you.R
import br.com.jlcampos.movie2you.data.model.Movie
import br.com.jlcampos.movie2you.databinding.ActivityDetailMovieBinding
import br.com.jlcampos.movie2you.presentation.adapter.MoviesAdapter
import br.com.jlcampos.movie2you.utils.Constants
import br.com.jlcampos.movie2you.utils.Status
import com.squareup.picasso.Picasso
import java.util.*

class DetailMovieActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityDetailMovieBinding
    private lateinit var viewModel: DetailMovieViewModel
    private lateinit var lastId: String

    private var mAdapter = MoviesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(DetailMovieViewModel::class.java)

        myUI()
        myObservers()

        getMovieAndList(viewModel.getRandomId()) // Buscando um id randomico de 100 a 150 excluindo os ids 119, 130 e 131
    }

    override fun onClick(view: View) {
        when(view) {
            binding.detailIvMyLike -> {
                if (viewModel.hasFav(lastId)) {
                    viewModel.removeFavorites(lastId)
                } else {
                    viewModel.addFav(lastId)
                }

                changeFav()
            }
        }
    }

    private fun changeFav() {
        binding.detailIvMyLike.setImageResource(if (viewModel.hasFav(lastId)) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24)
    }

    private fun myUI() {

        upDateListRv()

        binding.detailIvMyLike.setOnClickListener(this)

        mAdapter.setOnItemClickListener { movie, _ ->
            getMovieAndList(movie.id.toString())
        }
    }

    private fun getMovieAndList(id: String) {
        lastId = id
        viewModel.newPage = 1
        viewModel.getMovie(id)
        getSimiar(id)
    }

    private fun getSimiar(id: String) {
        viewModel.getSimilarList(id)
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

        viewModel.similarListLiveData.observe(this, {
            it?.let { myResource ->
                when(myResource.status) {
                    Status.SUCCESS -> {
                        hideProgressSimilar()

                        myResource.data?.let { mData ->

                            mAdapter.differ.submitList(mData.similarList)
                            val totalPages = mData.totalResults / Constants.QUERY_PAGE_SIZE + 2
                            isLastPage = viewModel.newPage == totalPages

                            if (isLastPage) {
                                binding.detailRvSimilar.setPadding(0,0,0,0)
                            }

                        }
                    }

                    Status.ERROR -> {
                        hideProgressSimilar()
                        wrong(myResource.message!!)
                    }

                    Status.LOADING -> {
                        showProgressSimilar()
                    }
                }
            }
        })
    }

    private fun hideProgressMovie() {
        binding.detailPbBackdrop.visibility = View.GONE
    }

    private fun showProgressMovie() {
        binding.detailPbBackdrop.visibility = View.VISIBLE
    }

    private fun hideProgressSimilar() {
        binding.detailPbSimilar.visibility = View.GONE
        isLoading = false
    }

    private fun showProgressSimilar() {
        binding.detailPbSimilar.visibility = View.VISIBLE
        isLoading = true
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

        changeFav()
    }

    private fun upDateListRv() {
        mAdapter = MoviesAdapter()
        binding.detailRvSimilar.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(this@DetailMovieActivity)
            addOnScrollListener(scrollListener)
        }
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private var scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate) {
                getSimiar(lastId)
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }
    }

    private fun wrong(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }






/*

    private var  mScaleFactor = 1f

    private val scaleLister = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector?): Boolean {
            mScaleFactor *= detector.scaleFactor

            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f))

            invalidate()
            return true
        }
    }

    private val mScaleDetector = ScaleGestureDetector(applicationContext, scaleLister)

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        mScaleDetector.onTouchEvent(event)
        return true
    }

*/

}