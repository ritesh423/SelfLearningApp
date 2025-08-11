package com.example.myapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentDetailsBinding
import com.example.myapplication.utils.ApiConstants
import com.example.myapplication.viewmodels.DetailsViewModel
import com.google.android.material.chip.Chip

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailsViewModel by viewModels()
    private val args: DetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Back button in toolbar
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        // Kick off loading
        viewModel.loadDetails(args.movieId)

        // Observe and bind
        viewModel.details.observe(viewLifecycleOwner) { detail ->
            // Collapsing toolbar title
            binding.collapsingToolbar.title = detail.title

            // Backdrop
            Glide.with(this)
                .load(ApiConstants.IMAGE_BASE_URL + detail.backdropPath)
                .into(binding.ivBackdrop)

            // Poster
            Glide.with(this)
                .load(ApiConstants.IMAGE_BASE_URL + detail.posterPath)
                .placeholder(R.drawable.ic_launcher_background)
                .into(binding.ivPosterDetail)

            // Text fields
            binding.tvTitleDetail.text   = detail.title
            binding.tvTaglineDetail.text = detail.tagline
            binding.tvReleaseDate.text   = "Release: ${detail.releaseDate}"
            binding.tvRuntime.text       = "Runtime: ${detail.runtime} min"
            binding.tvOverview.text      = detail.overview

            // Rating + votes
            binding.ratingBar.rating   = (detail.voteAverage / 2f).toFloat()
            binding.tvRatingCount.text = "${detail.voteAverage} (${detail.voteCount} votes)"

            // Genres → Chips
            binding.chipGroupGenres.removeAllViews()
            detail.genres.forEach { genre ->
                val chip = Chip(requireContext()).apply {
                    text = genre.name
                    isClickable = false
                    isCheckable = false
                    chipBackgroundColor =
                        ContextCompat.getColorStateList(context, R.color.material_grey_200)
                    setTextColor(ContextCompat.getColor(context, R.color.black))
                }
                binding.chipGroupGenres.addView(chip)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
