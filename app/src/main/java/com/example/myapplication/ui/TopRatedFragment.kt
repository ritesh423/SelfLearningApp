package com.example.myapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.adapters.MovieAdapter
import com.example.myapplication.databinding.FragmentTopRatedBinding
import com.example.myapplication.viewmodels.MoviesUiState
import com.example.myapplication.viewmodels.MoviesViewModel

class TopRatedFragment : Fragment() {

    private var _binding: FragmentTopRatedBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MoviesViewModel by viewModels()
    private lateinit var adapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTopRatedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView
        adapter = MovieAdapter(onItemClick = { /* navigate to details if you want */ })
        binding.rvMovies.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvMovies.adapter = adapter

        // Initial state: show loader, hide list
        binding.pbLoading.visibility = View.VISIBLE
        binding.rvMovies.visibility = View.GONE

        // Observe UI state
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is MoviesUiState.Loading -> {
                    binding.pbLoading.visibility = View.VISIBLE
                    binding.rvMovies.visibility = View.GONE
                }
                is MoviesUiState.Success -> {
                    adapter.submitList(state.data)
                    binding.pbLoading.visibility = View.GONE
                    binding.rvMovies.visibility = View.VISIBLE
                }
                is MoviesUiState.Error -> {
                    binding.pbLoading.visibility = View.GONE
                    binding.rvMovies.visibility = View.GONE
                    // TODO show error UI / Snackbar with state.message
                }
            }
        }

        // Trigger load AFTER observer is set
        viewModel.getTopRatedMovies(pageNo = 1 /*, minShimmerMs = 800L */)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
