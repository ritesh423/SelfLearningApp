package com.example.myapplication.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.adapters.MovieAdapter
import com.example.myapplication.databinding.FragmentPopularBinding
import com.example.myapplication.viewmodels.MoviesUiState
import com.example.myapplication.viewmodels.MoviesViewModel

class PopularFragment : Fragment() {

    private var _binding: FragmentPopularBinding? = null
    private val binding get() = _binding!!
    private val TAG = "PopularFragment"

    private val viewModel: MoviesViewModel by viewModels()
    private lateinit var adapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPopularBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView setup
        adapter = MovieAdapter { movie ->
            val action = PopularFragmentDirections
                .actionMenuPopularToArticleFragment(movie.id)
            findNavController().navigate(action)
        }
        binding.rvMovies.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvMovies.adapter = adapter

        // Initial UI state
        Log.d(TAG, "onViewCreated: show shimmer & hide list")
        binding.rvMovies.visibility = View.GONE
        binding.pbLoading.visibility = View.GONE
        binding.shimmer.visibility = View.VISIBLE
        binding.shimmer.startShimmer()

        // Observe UI state
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is MoviesUiState.Loading -> {
                    Log.d(TAG, "state=Loading → start shimmer")
                    binding.rvMovies.visibility = View.GONE
                    binding.shimmer.visibility = View.VISIBLE
                    binding.shimmer.startShimmer()
                }
                is MoviesUiState.Success -> {
                    Log.d(TAG, "state=Success → items=${state.data.size}; stop shimmer & show list")
                    adapter.submitList(state.data)
                    binding.shimmer.stopShimmer()
                    binding.shimmer.visibility = View.GONE
                    binding.rvMovies.visibility = View.VISIBLE
                }
                is MoviesUiState.Error -> {
                    Log.d(TAG, "state=Error → ${state.message}; stop shimmer & keep list hidden")
                    binding.shimmer.stopShimmer()
                    binding.shimmer.visibility = View.GONE
                    binding.rvMovies.visibility = View.GONE
                    // TODO: show error UI / Snackbar here
                }
            }
        }

        // Trigger load AFTER observer is set
        Log.d(TAG, "onViewCreated: calling getPopularMovies()")
        viewModel.getPopularMovies(pageNo = 1 /*, minShimmerMs = 800L */)
    }

    override fun onStart() {
        super.onStart()
        // If still loading when returning to foreground, keep shimmer animating
        if (binding.shimmer.visibility == View.VISIBLE && binding.rvMovies.visibility != View.VISIBLE) {
            Log.d(TAG, "onStart: still loading → (re)start shimmer")
            binding.shimmer.startShimmer()
        }
    }

    override fun onStop() {
        // Avoid running animation offscreen
        Log.d(TAG, "onStop: stop shimmer to avoid offscreen anim")
        binding.shimmer.stopShimmer()
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
