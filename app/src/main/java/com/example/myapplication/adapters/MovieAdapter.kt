package com.example.myapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.network.Movie
import com.example.myapplication.utils.ApiConstants


class MovieAdapter(
    private val onItemClick: (Movie) -> Unit
) : ListAdapter<Movie, MovieAdapter.MovieViewHolder>(MovieDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivPoster: ImageView = itemView.findViewById(R.id.ivPoster)
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvRating: TextView = itemView.findViewById(R.id.tvRating)

        fun bind(movie: Movie) {
            // Title
            tvTitle.text = movie.title

            // Rating (★ symbol + voteAverage)
            tvRating.text = "★ ${movie.voteAverage}"

            // Load poster
            Glide.with(itemView)
                .load(ApiConstants.IMAGE_BASE_URL + movie.posterPath)
                .placeholder(R.drawable.ic_launcher_background)
                .into(ivPoster)

            // click
            itemView.setOnClickListener { onItemClick(movie) }
        }
    }
}

private class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(old: Movie, new: Movie) = old.id == new.id
    override fun areContentsTheSame(old: Movie, new: Movie) = old == new
}
