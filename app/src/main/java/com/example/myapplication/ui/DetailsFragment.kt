package com.example.myapplication.ui

import DetailsViewModel
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentDetailsBinding
import com.example.myapplication.utils.ApiConstants
import com.google.android.material.chip.Chip

class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private val viewModel: DetailsViewModel by viewModels()
    private val args: DetailsFragmentArgs by navArgs()

    private var player: ExoPlayer? = null
    private var playWhenReady = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadDetails(args.movieId)

        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        viewModel.details.observe(viewLifecycleOwner) { d ->
            binding.collapsingToolbar.title = d.title ?: ""
            binding.tvTitleDetail.text = d.title ?: ""
            binding.tvTaglineDetail.text = d.tagline ?: ""
            binding.tvOverview.text = d.overview

            binding.tvReleaseDate.text = "Release: ${d.releaseDate ?: "-"}"
            binding.tvRuntime.text = d.runtime?.let { "${it} min" } ?: "-"

            val voteAvg = d.voteAverage ?: 0.0
            binding.ratingBar.rating = (voteAvg / 2f).toFloat()
            binding.tvRatingCount.text = "${"%.1f".format(voteAvg)} (${d.voteCount ?: 0})"

            binding.chipGroupGenres.removeAllViews()
            d.genres.orEmpty().forEach { g ->
                val chip = Chip(requireContext()).apply {
                    text = g.name
                    isCheckable = false
                }
                binding.chipGroupGenres.addView(chip)
            }
        }

        viewModel.trailer.observe(viewLifecycleOwner) { t ->
            val playable = t.playableUrl
            val ytKey = t.youTubeKey

            if (playable != null) {
                // Show video
                binding.ivBackdrop.visibility = View.GONE
                binding.btnWatchTrailer.visibility = View.GONE
                initPlayer(playable)
            } else {
                // Show backdrop + button
                binding.ivBackdrop.visibility = View.VISIBLE

                viewModel.details.value?.let { d ->
                    val backdropPath = d.backdropPath ?: d.posterPath
                    if (backdropPath != null) {
                        Glide.with(this)
                            .load(ApiConstants.IMAGE_BASE_URL + backdropPath)
                            .centerCrop()
                            .into(binding.ivBackdrop)
                    }
                }

                if (!ytKey.isNullOrBlank()) {
                    binding.btnWatchTrailer.visibility = View.VISIBLE
                    binding.btnWatchTrailer.setOnClickListener {
                        val uri = Uri.parse("https://www.youtube.com/watch?v=$ytKey")
                        startActivity(Intent(Intent.ACTION_VIEW, uri))
                    }
                } else {
                    binding.btnWatchTrailer.visibility = View.GONE
                }
            }
        }

        binding.btnShare.setOnClickListener {
            val d = viewModel.details.value ?: return@setOnClickListener
            val text = "${d.title ?: ""}\n${d.overview}"
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, text)
            }
            startActivity(Intent.createChooser(intent, "Share"))
        }
    }

    private fun initPlayer(url: String) {
        releasePlayer()
        val exo = ExoPlayer.Builder(requireContext()).build()
        player = exo
        binding.playerView.player = exo

        val mediaItem = MediaItem.fromUri(Uri.parse(url))
        exo.setMediaItem(mediaItem)
        exo.playWhenReady = playWhenReady
        exo.prepare()
        exo.play()
    }

    private fun releasePlayer() {
        player?.let {
            playWhenReady = it.playWhenReady
            it.release()
        }
        player = null
        binding.playerView.player = null
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
        (requireActivity() as AppCompatActivity).supportActionBar?.show(); super.onStop()
    }

    override fun onDestroyView() {
        releasePlayer()
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume(); (requireActivity() as AppCompatActivity).supportActionBar?.hide()
    }

}
