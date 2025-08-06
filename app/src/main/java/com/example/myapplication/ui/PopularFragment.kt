package com.example.myapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.adapters.MovieAdapter
import com.example.myapplication.databinding.FragmentPopularBinding
import com.example.myapplication.viewmodels.MoviesViewModel

class PopularFragment : Fragment() {

    private var _binding: FragmentPopularBinding? = null
    private val binding get() = _binding!!

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

        // 1) ensure we see only shimmer at start
        binding.rvMovies.visibility = View.GONE
        binding.pbLoading.visibility = View.GONE
        binding.shimmer.startShimmer()

        // 2) kick off the network load
        viewModel.getPopularMovies(pageNo = 1)

        // 3) setup our adapter
        adapter = MovieAdapter { movie ->
            val action = PopularFragmentDirections
                .actionMenuPopularToArticleFragment(movie.id)
            findNavController().navigate(action)
        }
        binding.rvMovies.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvMovies.adapter = adapter

        // 4) when the data arrives, swap out
        viewModel.movies.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            binding.shimmer.apply {
                stopShimmer()
                visibility = View.GONE
            }
            binding.rvMovies.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
