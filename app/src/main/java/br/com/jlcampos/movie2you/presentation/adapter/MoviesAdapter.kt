package br.com.jlcampos.movie2you.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import br.com.jlcampos.movie2you.R
import br.com.jlcampos.movie2you.data.model.Movie
import com.squareup.picasso.Picasso

class MoviesAdapter: RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bindView(differ.currentList[position], position)
    }

    private val differCallback = object : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    private var onItemClickListener: ((Movie, Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (Movie, Int) -> Unit) {
        onItemClickListener = listener
    }

    override fun getItemCount() = differ.currentList.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val img = view.findViewById<ImageView>(R.id.item_movie_iv_poster)
        private val title = view.findViewById<TextView>(R.id.item_movie_tv_title)
        private val year = view.findViewById<TextView>(R.id.item_movie_tv_year)
        private val genres = view.findViewById<TextView>(R.id.item_movie_tv_genres)

        fun bindView(movie: Movie, position: Int) {
            Picasso.get().load(movie.poster).error(R.drawable.ic_baseline_image_24).into(img)
            title.text = movie.originalTitle
            year.text = movie.releaseDate

            itemView.setOnClickListener{
                onItemClickListener?.let { it(movie, position) }
            }
        }
    }

}