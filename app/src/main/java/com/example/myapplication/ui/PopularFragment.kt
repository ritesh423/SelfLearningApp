package com.example.myapplication.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.adapters.MovieAdapter
import com.example.myapplication.databinding.FragmentPopularBinding
import com.example.myapplication.viewmodels.MoviesViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class PopularFragment : Fragment() {

private var _binding: FragmentPopularBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
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
        viewModel.getPopularMovies(pageNo = 1)
        adapter = MovieAdapter { movie ->
            val action = PopularFragmentDirections.
            actionMenuPopularToArticleFragment(movie.id)
            findNavController().navigate(action)
        }
        binding.rvMovies.layoutManager= GridLayoutManager(requireContext(), 2)
        binding.rvMovies.adapter = adapter
        viewModel.movies.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            binding.pbLoading.visibility = View.GONE
        }

    }

override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}